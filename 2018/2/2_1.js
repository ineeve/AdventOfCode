const fs = require('fs');

fs.readFile('input','utf8', (err, data) => {
    let strings = data.split("\r\n");
    let totalTwos = 0, totalThrees = 0;
    strings.forEach(s => {
        let charFrequency = {};
        let chars = s.split('');
        chars.forEach(c => {
            charFrequency[c] ? charFrequency[c]++ : charFrequency[c] = 1;
        });
        let frequencyValues = Object.values(charFrequency);
        let foundTwo, foundThree;
        frequencyValues.forEach(v => {
            if (v == 2) foundTwo = true;
            else if (v == 3) foundThree = true;
        })
        if (foundTwo) totalTwos++;
        if (foundThree) totalThrees++;
    })
    console.log("Total 2: " + totalTwos);
    console.log("Total 3: " + totalThrees);
    console.log("Result: " + totalTwos * totalThrees);
})