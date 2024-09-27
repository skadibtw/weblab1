const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');

const R = 200; // Радиус

// O
const centerX = canvas.width / 2;
const centerY = canvas.height / 2;

// circle
ctx.beginPath();
ctx.moveTo(centerX, centerY);
ctx.arc(centerX, centerY, R/2, 3 * Math.PI/2, 0 , false); // Круговая дуга
ctx.lineTo(centerX, centerY);
ctx.closePath();
ctx.fillStyle = 'blue';
ctx.fill();

// triangle
ctx.beginPath();
ctx.moveTo(centerX, centerY);
ctx.lineTo(centerX - R, centerY);
ctx.lineTo(centerX, centerY + R / 2);
ctx.closePath();
ctx.fillStyle = 'blue';
ctx.fill();

// square
ctx.beginPath();
ctx.rect(centerX, centerY, R, R);
ctx.fillStyle = 'blue';
ctx.fill();

// text
ctx.fillStyle = 'black';
ctx.font = '16px sans-serif';

// R on graph
ctx.fillText('R', centerX + R, centerY - 5);
ctx.fillText('R', centerX - 15, centerY - R);
ctx.fillText('-R', centerX - R - 15, centerY - 5);
ctx.fillText('-R', centerX - 15, centerY + R + 5);

// R / 2 on graph
ctx.fillText('R/2', centerX + R / 2, centerY - 5);
ctx.fillText('R/2', centerX - 15, centerY - R / 2);
ctx.fillText('-R/2', centerX - R / 2 - 20, centerY - 5);
ctx.fillText('-R/2', centerX - 15, centerY + R / 2 + 5);
// X
ctx.beginPath();
ctx.moveTo(0, centerY);
ctx.lineTo(canvas.width, centerY);
ctx.strokeStyle = 'black';
ctx.lineWidth = 2;
ctx.stroke();

// Y
ctx.beginPath();
ctx.moveTo(centerX, 0);
ctx.lineTo(centerX, canvas.height);
ctx.strokeStyle = 'black';
ctx.lineWidth = 2;
ctx.stroke();
// 0 in the middle
ctx.fillText('0', centerX + 5, centerY - 5);