const clearButton = document.getElementById("clear");
const resultBody = document.getElementById("resultBody");
const yInput = document.getElementById("y");
const rInput = document.getElementById("r");

const epsilon = 1e-10;

let results = JSON.parse(localStorage.getItem("results") || "[]");

drawGraph();

renderRows();

document.querySelectorAll('input[name="x"]').forEach((input) => {
  input.addEventListener("change", drawInputPoint);
});

yInput.addEventListener("input", (event) => {
  event.target.value = event.target.value.replace(/[a-zA-ZА-Яа-я]/g, "");
  drawInputPoint();
});

rInput.addEventListener("input", (event) => {
  event.target.value = event.target.value.replace(/[a-zA-ZА-Яа-я]/g, "");
  drawInputPoint();
});

clearButton.addEventListener("click", function () {
  results = [];
  localStorage.removeItem("results");
  resultBody.replaceChildren();
});

document
  .getElementById("myForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault();

    removeErrors();
    let wasError = false;

    const formData = new FormData(event.target);

    const xString = formData.get("x");
    if (xString == null) {
      showErrorX("Выберите X");
      wasError = true;
    }

    const yString = formData.get("y");

    if (yString == "") {
      showErrorY("Вы забыли вписать Y");
      wasError = true;
    }

    const rString = formData.get("r");

    if (rString == "") {
      showErrorR("Вы забыли вписать R");
      wasError = true;
    }

    const x = parseInt(formData.get("x"));
    const y = parseFloat(formData.get("y"));
    const r = parseFloat(formData.get("r"));

    if (isNaN(y)) {
      showErrorY("Y должен быть числом");
      wasError = true;
    }

    const isYInBounds = y >= -5 && y <= 3;
    if (!isYInBounds) {
      showErrorY("Y должен быть от -5 до 3 (включительно)");
      wasError = true;
    }

    if (isNaN(r)) {
      showErrorR("R должен быть числом");
      wasError = true;
    }

    const isRInBounds = r >= 2 && r <= 5;
    if (!isRInBounds) {
      showErrorR("R должен быть от 2 до 5 (включительно)");
      wasError = true;
    }

    if (wasError) {
      return;
    }

    const response = await fetch("/fcgi-bin/backend.jar", {
      method: "POST",
      body: new URLSearchParams(formData),
    });
    console.log("send");

    const result = await response.json();
    console.log(result);

    results.push(result);

    localStorage.setItem("results", JSON.stringify(results));

    addRow(result);
  });

function drawGraph(r) {
  const c = document.getElementById("myCanvas");
  const width = parseInt(c.getAttribute("width"));
  const arrowLength = width * 0.02;

  const ctx = c.getContext("2d");

  ctx.clearRect(0, 0, width, width);

  const fontSize = width * 0.05;
  ctx.font = fontSize + "px Arial";

  const scale = width / 10;

  const hasR =
    typeof r === "number" && !isNaN(r) && parseInt(r) >= 2 && parseInt(r) <= 5;

  const roundedR = Math.round(r * 10) / 10;
  const roundedHalfR = Math.round((r / 2) * 10) / 10;

  const labelR = hasR ? String(roundedR) : "R";
  const labelR2 = hasR ? String(roundedHalfR) : "R/2";
  const labelNegR = hasR ? String(-roundedR) : "-R";
  const labelNegR2 = hasR ? String(-roundedHalfR) : "-R/2";

  ctx.textAlign = "right";

  const basePixels = 4 * scale;

  ctx.fillStyle = "#a1bc98";

  ctx.beginPath();
  ctx.moveTo(width / 2, width / 2);
  ctx.arc(width / 2, width / 2, basePixels / 2, Math.PI, Math.PI * 1.5);
  ctx.closePath();
  ctx.fill();

  ctx.beginPath();
  ctx.moveTo(width * 0.5 - basePixels, width * 0.5);
  ctx.lineTo(width * 0.5, width * 0.5 + basePixels);
  ctx.lineTo(width * 0.5, width * 0.5);
  ctx.closePath();
  ctx.fill();

  ctx.fillRect(width * 0.5, width * 0.5, basePixels, basePixels / 2);

  ctx.fillStyle = "black";

  ctx.beginPath();
  let fromX = 0;
  let fromY = width / 2;
  let toX = width;
  let toY = width / 2;
  ctx.moveTo(fromX, fromY);
  ctx.lineTo(toX, toY);
  ctx.lineTo(toX - arrowLength, toY + arrowLength);
  ctx.moveTo(toX, toY);
  ctx.lineTo(toX - arrowLength, toY - arrowLength);

  fromX = width / 2;
  fromY = width;
  toX = width / 2;
  toY = 0;

  ctx.moveTo(fromX, fromY);
  ctx.lineTo(toX, toY);
  ctx.lineTo(toX - arrowLength, toY + arrowLength);
  ctx.moveTo(toX, toY);
  ctx.lineTo(toX + arrowLength, toY + arrowLength);

  ctx.stroke();

  ctx.fillText("y", width * 0.47, width * 0.03);
  ctx.fillText("x", width * 0.97, width * 0.48);

  ctx.moveTo(width * 0.49, width / 2 - basePixels);
  ctx.lineTo(width * 0.51, width / 2 - basePixels);
  ctx.fillText(labelR, width * 0.47, width / 2 - basePixels + fontSize * 0.3);

  ctx.moveTo(width * 0.49, width / 2 - basePixels / 2);
  ctx.lineTo(width * 0.51, width / 2 - basePixels / 2);
  ctx.fillText(
    labelR2,
    width * 0.47,
    width / 2 - basePixels / 2 + fontSize * 0.3,
  );

  ctx.moveTo(width * 0.49, width / 2 + basePixels / 2);
  ctx.lineTo(width * 0.51, width / 2 + basePixels / 2);
  ctx.fillText(
    labelNegR2,
    width * 0.47,
    width / 2 + basePixels / 2 + fontSize * 0.3,
  );

  ctx.moveTo(width * 0.49, width / 2 + basePixels);
  ctx.lineTo(width * 0.51, width / 2 + basePixels);
  ctx.fillText(
    labelNegR,
    width * 0.47,
    width / 2 + basePixels + fontSize * 0.3,
  );

  ctx.moveTo(width / 2 - basePixels, width * 0.49);
  ctx.lineTo(width / 2 - basePixels, width * 0.51);
  ctx.fillText(
    labelNegR,
    width / 2 - basePixels + fontSize * 0.3,
    width * 0.57,
  );

  ctx.moveTo(width / 2 - basePixels / 2, width * 0.49);
  ctx.lineTo(width / 2 - basePixels / 2, width * 0.51);
  ctx.fillText(
    labelNegR2,
    width / 2 - basePixels / 2 + fontSize * 0.5,
    width * 0.57,
  );

  ctx.moveTo(width / 2 + basePixels / 2, width * 0.49);
  ctx.lineTo(width / 2 + basePixels / 2, width * 0.51);
  ctx.fillText(
    labelR2,
    width / 2 + basePixels / 2 + fontSize * 0.3,
    width * 0.57,
  );

  ctx.moveTo(width / 2 + basePixels, width * 0.49);
  ctx.lineTo(width / 2 + basePixels, width * 0.51);
  ctx.fillText(labelR, width / 2 + basePixels + fontSize * 0.3, width * 0.57);

  ctx.stroke();
}

function drawInputPoint() {
  const xInput = document.querySelector('input[name="x"]:checked');

  const x = xInput ? parseInt(xInput.value) : NaN;
  const y = parseFloat(yInput.value);
  const r = parseFloat(rInput.value);

  if (isNaN(x) || isNaN(y) || isNaN(r)) {
    drawGraph(r);
    return;
  }

  drawGraph(r);
  drawPoint(x, y, r);
}

function drawPoint(x, y, r) {
  const c = document.getElementById("myCanvas");
  const width = parseInt(c.getAttribute("width"));
  const ctx = c.getContext("2d");

  const basePixels = (width / 10) * 4;
  const scale = basePixels / r;

  const centerX = width / 2;
  const centerY = width / 2;

  const canvasX = centerX + x * scale;
  const canvasY = centerY - y * scale;

  ctx.beginPath();
  ctx.arc(canvasX, canvasY, width * 0.015, 0, Math.PI * 2);
  ctx.fillStyle = "#2b3328";
  ctx.fill();
}

function renderRows() {
  resultBody.innerHTML = "";

  results.forEach(addRow);
}

function addRow(result) {
  const row = document.createElement("tr");

  const numberCell = document.createElement("td");
  numberCell.textContent = resultBody.rows.length + 1;

  const xCell = document.createElement("td");
  xCell.textContent = result.x;

  const yCell = document.createElement("td");
  yCell.textContent = result.y;

  const rCell = document.createElement("td");
  rCell.textContent = result.r;

  const checkingResultCell = document.createElement("td");
  checkingResultCell.textContent = result.checkingResult ? "Да" : "Нет";

  const timestamp = document.createElement("td");
  timestamp.textContent = formatDate(result.timestamp);

  const executionTimeMs = document.createElement("td");
  executionTimeMs.textContent = result.executionTimeMs;

  row.append(
    numberCell,
    xCell,
    yCell,
    rCell,
    checkingResultCell,
    timestamp,
    executionTimeMs,
  );
  resultBody.appendChild(row);
}

function checkUpperLeftCorner(x, y, r) {
  if (x <= 0 && x >= -r / 2 && y >= 0) {
    return (r * r) / 4 >= x * x + y * y;
  }
  return false;
}

function checkBottomLeftCorner(x, y, r) {
  if (x <= 0 && x >= -r && y <= 0) {
    return y >= -x - r;
  }
  return false;
}

function checkBottomRightCorner(x, y, r) {
  if (x >= 0 && x <= r && y <= 0 && y >= -r / 2) {
    return true;
  }

  return false;
}

function formatDate(timestamp) {
  return new Intl.DateTimeFormat("ru-RU", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(timestamp));
}

function showErrorX(errorText) {
  const existingError = document.getElementById("errorX");

  if (existingError && existingError.textContent == "") {
    existingError.textContent = errorText;
    return;
  }
}

function showErrorY(errorText) {
  const existingError = document.getElementById("errorY");

  if (existingError && existingError.textContent == "") {
    existingError.textContent = errorText;
    return;
  }
}

function showErrorR(errorText) {
  const existingError = document.getElementById("errorR");

  if (existingError && existingError.textContent == "") {
    existingError.textContent = errorText;
    return;
  }
}

function removeErrors() {
  const existingErrorX = document.getElementById("errorX");
  const existingErrorY = document.getElementById("errorY");
  const existingErrorR = document.getElementById("errorR");

  if (existingErrorX) {
    existingErrorX.textContent = "";
  }
  if (existingErrorY) {
    existingErrorY.textContent = "";
  }
  if (existingErrorR) {
    existingErrorR.textContent = "";
  }
  return;
}
