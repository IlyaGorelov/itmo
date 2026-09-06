var c = document.getElementById("myCanvas");
var width = parseInt(c.getAttribute("width"));
var arrowLength = width * 0.02;

var ctx = c.getContext("2d");

var fontSize = width * 0.05;
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
fromx = 0;
fromy = width / 2;
tox = width;
toy = width / 2;
ctx.moveTo(fromx, fromy);
ctx.lineTo(tox, toy);
ctx.lineTo(tox - arrowLength, toy + arrowLength);
ctx.moveTo(tox, toy);
ctx.lineTo(tox - arrowLength, toy - arrowLength);

//vertical arrow
fromx = width / 2;
fromy = width;
tox = width / 2;
toy = 0;

ctx.moveTo(fromx, fromy);
ctx.lineTo(tox, toy);
ctx.lineTo(tox - arrowLength, toy + arrowLength);
ctx.moveTo(tox, toy);
ctx.lineTo(tox + arrowLength, toy + arrowLength);

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
