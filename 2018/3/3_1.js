const fs = require('fs');

let matrix = new Array(1000);

for (let i = 0; i < matrix.length; i++) {
  matrix[i] = new Array(1000);
}

fs.readFile('input','utf8', (err, data) => {
    let lines = data.split('\r\n');
    let result = parseLines(lines);
    console.log('Result: ', result);
});

function parseLines(lines){
    let id = 1;
    let overlapCounter = 0;
    lines.forEach(line => {
        let col,row,width,height;
        let lineData = line.split('@ ')[1];
        let startPosAndSizes = lineData.split(':');
        let startPos = startPosAndSizes[0];
        let sizes = startPosAndSizes[1].trim();
        col = startPos.split(',')[0];
        row = startPos.split(',')[1];
        width = sizes.split('x')[0];
        height = sizes.split('x')[1];
        overlapCounter += markMatrix(id, +col, +row, +width, +height);
        id++;
    });
    return overlapCounter;
}

function markMatrix(id, colI, rowI, width, height){
    let overlapCounter = 0;
    let lastCol = colI + width;
    let lastRow = rowI + height;
    for (let col = colI; col < lastCol; col++){
        for (let row = rowI; row < lastRow; row++){
            let matrixPrevValue = matrix[row][col];
            if (matrixPrevValue == null){
                matrix[row][col]=id;
            } else if (matrixPrevValue != 'x'){
                matrix[row][col]='x';
                overlapCounter++;
            }
        }
    }
    return overlapCounter;
}
