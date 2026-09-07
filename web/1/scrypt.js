const xButton = document.getElementById("increaseXButton");
const clearButton = document.getElementById("clear");
const xElement = document.getElementById("currentX");
const resultBody = document.getElementById("resultBody");

let results = JSON.parse(localStorage.getItem("results") || "[]");

drawGraph();

renderRows();

clearButton.addEventListener("click", function () {
  results = [];
  localStorage.removeItem("results");
  resultBody.replaceChildren();
});

xButton.addEventListener("click", increaseX);

document.getElementById("myForm").addEventListener("submit", function (event) {
  event.preventDefault();

  console.log("debug 1");
  const formData = new FormData(event.target);

  const x = parseInt(xElement.textContent);
  const y = parseFloat(formData.get("changeY"));
  const r = parseInt(formData.get("changeR"));

  const checkingResult =
    checkBottomLeftCorner(x, y, r) ||
    checkBottomRightCorner(x, y, r) ||
    checkUpperLeftCorner(x, y, r);

  if (checkingResult) {
    alert("Попали");
  } else {
    alert(getRandomInsults());
  }

  const result = {
    x: x,
    y: y,
    r: r,
    checkingResult: checkingResult,
    time: Date.now(),
  };

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
  ctx.fillRect(width * 0.1, width * 0.5, width * 0.4, width * 0.2);

  ctx.beginPath();
  ctx.moveTo(width * 0.1, width * 0.5);
  ctx.lineTo(width * 0.5, width * 0.3);
  ctx.lineTo(width * 0.5, width * 0.5);
  ctx.closePath();
  ctx.fill();

  ctx.beginPath();
  ctx.moveTo(width / 2, width / 2);
  ctx.arc(width / 2, width / 2, width * 0.4, 0, Math.PI * 0.5);
  ctx.closePath();
  ctx.fill();

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

function increaseX() {
  let curX = parseInt(xElement.textContent);

  let newX = curX + 1;

  if (newX > 4) newX = -4;

  xElement.textContent = newX;
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
  if (x <= 0 && x >= -r && y >= 0) {
    return y <= x * 0.5 + 0.5;
  }
  return false;
}

function checkBottomLeftCorner(x, y, r) {
  if (x <= 0 && x >= -r && y <= 0) {
    return y >= -r / 2;
  }
  return false;
}

function checkBottomRightCorner(x, y, r) {
  if (x >= 0 && y <= 0) {
    return r * r <= x * x + y * y;
  }
  return false;
}

function formatDate(timestamp) {
  return new Intl.DateTimeFormat("ru-RU", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(timestamp));
}

function getRandomInsults() {
  const insults = [
    "Вы ни на что не годны",
    "Не стоило этого делать",
    "Это прям очень плохо",
    "У этого будут последствия",
    "Вам стоит вернуться в школу",
    "Моя кошка справилась бы с этим лучше",
    "Ваши родственники будут Вами недовольны",
  ];

  const random = Math.floor(Math.random() * insults.length);
  return insults[random];
}
