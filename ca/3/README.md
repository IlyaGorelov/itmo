# My option:

acc32: [wrench.edu](https://wrench.edu.swampbuds.me/report/8a10cfbc-da8e-4635-b75c-5679dfcd3f38)

```py
def capital_case_pstr(s: str) -> tuple[str | list[int], str]:
    """Convert the first character of each word in a Pascal string to capital case.

    Capital Case Is Something Like This.

    - Result string should be represented as a correct Pascal string.
    - Buffer size for the message -- `0x20`, starts from `0x00`.
    - End of input -- new line.
    - Initial buffer values -- `_`.

    Python example args:
        s (str): The input string till new line.

    Returns:
        tuple: A tuple containing the capitalized output string and input rest.
    """
    line, rest = read_line(s, 0x20)
    if line is None:
        return [overflow_error_value], rest
    return line.title(), rest


assert capital_case_pstr('hello world\n') == ('Hello World', '')
# and mem[0..31]: 0b 48 65 6c 6c 6f 20 57 6f 72 6c 64 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f
assert capital_case_pstr('python programming\n') == ('Python Programming', '')
# and mem[0..31]: 12 50 79 74 68 6f 6e 20 50 72 6f 67 72 61 6d 6d 69 6e 67 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f 5f
```

f32a: [wrench.edu](https://wrench.edu.swampbuds.me/report/0c5aa796-b81f-4091-9387-f238c5f1d004)

```py
def count_leading_zeros(n: int) -> int:
"""Count the number of leading zeros in the binary representation of an integer.

    Args:
        n (int): The integer to count leading zeros for.

    Returns:
        int: The number of leading zeros.
    """
    if n == 0:
        return 32
    count = 0
    for i in range(31, -1, -1):
        if (n >> i) & 1 == 0:
            count += 1
        else:
            break
    return count

assert count_leading_zeros(1) == 31
assert count_leading_zeros(2) == 30
assert count_leading_zeros(16) == 27
```

m68k: [wrench.edu](https://wrench.edu.swampbuds.me/report/a414da27-4cf0-4896-82cc-865570494b96)

```py
def format_string(input: str) -> tuple[str | list[int], str]:
"""Format string with %d placeholders replaced by integers from input.

    Input format: "format_string\\nint1\\nint2\\n..."
    Examples:
    - "Foo %d bar %d\\n232\\n43\\n" -> "Foo 232 bar 43"
    - "%5d\\n42\\n" -> "   42" (right-aligned, 5 digits)
    - "%-5d\\n42\\n" -> "42   " (left-aligned, 5 digits)
    - "Just text\\n" -> "Just text" (no formatting)

    Format string input buffer size limit: 0x20 bytes
    Output: unlimited size

    Integer handling: Only accepts 32-bit signed integers (-2147483648 to 2147483647).
    Returns -1 if any integer is outside this range.

    Returns formatted string or error codes:
    - -1 for invalid input format or format string exceeds 0x20 bytes
    """
    try:
        lines = input.split("\n")
        if len(lines) < 1:
            return [-1], input

        format_str = lines[0]

        # Check format string buffer size limit (0x20 bytes)
        format_bytes = 0
        overflow_idx = None
        for idx, ch in enumerate(format_str):
            format_bytes += len(ch.encode("utf-8"))
            if format_bytes > 0x20:
                overflow_idx = idx
                break
        if overflow_idx is not None:
            remaining = input[overflow_idx + 1 :]
            return [-1], remaining

        # Find all format specifiers: %d, %5d, %-5d, etc.
        format_specs = []
        i = 0
        while i < len(format_str):
            if format_str[i] == "%":
                spec_start = i
                i += 1
                if i < len(format_str) and format_str[i] == "-":
                    i += 1
                while i < len(format_str) and format_str[i].isdigit():
                    i += 1
                if i < len(format_str) and format_str[i] == "d":
                    format_specs.append(format_str[spec_start : i + 1])
                    i += 1
                else:
                    i = spec_start + 1
            else:
                i += 1
        placeholder_count = len(format_specs)

        # Check if we have enough lines for the placeholders
        if placeholder_count > 0 and len(lines) < placeholder_count + 1:
            return [-1], input

        # Parse integers from remaining lines
        # Parse integers from remaining lines
        integers = []
        line_idx = 1
        for _ in range(placeholder_count):
            if line_idx >= len(lines):
                return [-1], input

            line = lines[line_idx]
            pos = 0
            sign = 1
            value = 0

            if pos < len(line) and line[pos] == "-":
                sign = -1
                pos += 1
            elif pos < len(line) and line[pos] == "+":
                pos += 1

            digit_start = pos

            while pos < len(line) and line[pos].isdigit():
                digit = ord(line[pos]) - ord("0")
                value = value * 10 + digit
                pos += 1

                # Check 32-bit boundary
                if sign == 1:
                    if value > 2147483647:
                        remaining = "\n".join([line[pos:]] + lines[line_idx + 1 :])
                        return [-1], remaining
                else:
                    if value > 2147483648:
                        remaining = "\n".join([line[pos:]] + lines[line_idx + 1 :])
                        return [-1], remaining

            if digit_start == pos:
                # Check if the line is empty (missing input) or invalid
                if pos < len(line):
                    # Non-empty invalid line - consume invalid character and return what's after
                    remaining = "\n".join([line[pos + 1 :]] + lines[line_idx + 1 :])
                else:
                    # Empty line - consume it and return what's after
                    remaining = (
                        "\n".join(lines[line_idx + 1 :])
                        if line_idx + 1 < len(lines)
                        else ""
                    )
                return [-1], remaining

            if pos < len(line):
                # Non-empty invalid line - consume invalid character and return what's after
                remaining = "\n".join([line[pos + 1 :]] + lines[line_idx + 1 :])
                return [-1], remaining

            parsed_int = sign * value
            integers.append(parsed_int)
            line_idx += 1

        # Format the string
        try:
            if placeholder_count == 0:
                result = format_str
            else:
                result = format_str % tuple(integers)
        except TypeError, ValueError:
            # Calculate remaining input
            remaining = "\n".join(lines[line_idx:]) if line_idx < len(lines) else ""
            return [-1], remaining

        # Calculate remaining input
        consumed_lines = line_idx
        if consumed_lines < len(lines):
            remaining = "\n".join(lines[consumed_lines:])
        else:
            remaining = ""

        return result, remaining

    except Exception:
        return [-1], input

assert format_string('Num: %d\n42\n') == ('Num: 42', '')
assert format_string('%5d\n42\n') == (' 42', '')
assert format_string('%-5d\n42\n') == ('42 ', '')
```

risc-iv: [wrench.edu](https://wrench.edu.swampbuds.me/report/d01d1425-0e01-41a8-a2c0-1fca43e2fc2d)

```py
def sum_odd_n(n: int) -> int:
    """Calculate the sum of odd numbers from 1 to n"""
    if n <= 0:
        return -1
    total = 0
    for i in range(1, n + 1):
        if i % 2 != 0:
            total += i
    return total

assert sum_odd_n(5) == 9
assert sum_odd_n(10) == 25
assert sum_odd_n(90000) == 2025000000
```

scheme: [link](https://github.com/IlyaGorelov/itmo/blob/685c97e4c972e49c1d83e81fba10d2a9b85628d0/ca/3/ca_scheme.drawio.png) `acc32-neumann[-pipeline-2]` - общая память; конвейер из 2 стадий (IF/EX)  
vliw: null

# Лабораторная работа №3. Опыты

[Full task](https://gitlab.se.ifmo.ru/computer-systems/csa-rolling/-/blob/master/lab3-task.md)
