const clearButton = document.getElementById("clear");
const resultBody = document.getElementById("resultBody");
const yInput = document.getElementById("y");
const rInput = document.getElementById("r");

const epsilon = 1e-10;

let results = JSON.parse(localStorage.getItem("results") || "[]");

drawGraph();

renderRows();

yInput.addEventListener("input", (event) => {
  event.target.value = event.target.value.replace(/[a-zA-ZА-Яа-я]/g, "");
});

rInput.addEventListener("input", (event) => {
  event.target.value = event.target.value.replace(/[a-zA-ZА-Яа-я]/g, "");
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

    const response = await fetch(
      "https://helios.cs.ifmo.ru:24443/fcgi-bin/backend.jar",
      {
        method: "POST",
        body: new URLSearchParams(formData),
      },
    );
    console.log("send");

    const result = await response.json();
    console.log(result);
    result.time = Date.now();

    results.push(result);

    localStorage.setItem("results", JSON.stringify(results));

    addRow(result);
  });

function drawGraph() {
  const c = document.getElementById("myCanvas");
  const width = parseInt(c.getAttribute("width"));
  const arrowLength = width * 0.02;

  const ctx = c.getContext("2d");

  const fontSize = width * 0.05;
  ctx.font = fontSize + "px Arial";

  // areas
  ctx.fillStyle = "lightblue";

  ctx.beginPath();
  ctx.moveTo(width / 2, width / 2);
  ctx.arc(width / 2, width / 2, width * 0.2, Math.PI, Math.PI * 1.5);
  ctx.closePath();
  ctx.fill();

  ctx.beginPath();
  ctx.moveTo(width * 0.1, width * 0.5);
  ctx.lineTo(width * 0.5, width * 0.9);
  ctx.lineTo(width * 0.5, width * 0.5);
  ctx.closePath();
  ctx.fill();

  ctx.fillRect(width * 0.5, width * 0.5, width * 0.4, width * 0.2);

  ctx.fillStyle = "black";

  ctx.beginPath();
  // horizontal arrow
  let fromX = 0;
  let fromY = width / 2;
  let toX = width;
  let toY = width / 2;
  ctx.moveTo(fromX, fromY);
  ctx.lineTo(toX, toY);
  ctx.lineTo(toX - arrowLength, toY + arrowLength);
  ctx.moveTo(toX, toY);
  ctx.lineTo(toX - arrowLength, toY - arrowLength);

  //vertical arrow
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

  ctx.fillText("y", width * 0.53, width * 0.03);
  ctx.fillText("x", width * 0.97, width * 0.48);

  // serifs at the vertical axis
  ctx.moveTo(width * 0.49, width * 0.1);
  ctx.lineTo(width * 0.51, width * 0.1);
  ctx.fillText("R", width * 0.53, width * 0.11);

  ctx.moveTo(width * 0.49, width * 0.3);
  ctx.lineTo(width * 0.51, width * 0.3);
  ctx.fillText("R/2", width * 0.53, width * 0.31);

  ctx.moveTo(width * 0.49, width * 0.7);
  ctx.lineTo(width * 0.51, width * 0.7);
  ctx.fillText("-R/2", width * 0.53, width * 0.71);

  ctx.moveTo(width * 0.49, width * 0.9);
  ctx.lineTo(width * 0.51, width * 0.9);
  ctx.fillText("-R", width * 0.53, width * 0.91);

  // serifs at the horiznotal axis
  ctx.moveTo(width * 0.1, width * 0.49);
  ctx.lineTo(width * 0.1, width * 0.51);
  ctx.fillText("R/2", width * 0.65, width * 0.47);

  ctx.moveTo(width * 0.3, width * 0.49);
  ctx.lineTo(width * 0.3, width * 0.51);
  ctx.fillText("R", width * 0.88, width * 0.47);

  ctx.moveTo(width * 0.7, width * 0.49);
  ctx.lineTo(width * 0.7, width * 0.51);
  ctx.fillText("-R", width * 0.08, width * 0.47);

  ctx.moveTo(width * 0.9, width * 0.49);
  ctx.lineTo(width * 0.9, width * 0.51);
  ctx.fillText("-R/2", width * 0.24, width * 0.47);

  ctx.stroke();
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

  const dateCell = document.createElement("td");
  dateCell.textContent = formatDate(result.time);

  row.append(numberCell, xCell, yCell, rCell, checkingResultCell, dateCell);
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
  const existingErrorY = document.getElementById("errorY");
  const existingErrorR = document.getElementById("errorR");

  if (existingErrorY) {
    existingErrorY.textContent = "";
  }
  if (existingErrorR) {
    existingErrorR.textContent = "";
  }
  return;
}
