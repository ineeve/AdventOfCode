const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

let frequencies = [];
rl.on('line', input => {
    frequencies.push(+input);
})
rl.on('SIGINT', () => {
    rl.close();
    let frequencyCount = 0;
    let frequencyMap = {0:1};
    let result;
    while(!result){
        for (let i = 0; i < frequencies.length; i++){
            frequencyCount += frequencies[i];
            if (frequencyMap[frequencyCount] === 1){
                result = frequencyCount;
                break;
            }
            frequencyMap[frequencyCount]=1;
        }
    }
    console.log("Result: " + result);
})
