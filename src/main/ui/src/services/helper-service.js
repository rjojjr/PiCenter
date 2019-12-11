export const dateStringFormat =(time) => {
    const date = new Date(time);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const year = date.getFullYear();
    return `${month}/${day}/${year}`;
}

export const chartTypes = (type) => {
    if (type.includes(/hu/)){
        return 'hum';
    }else{
        return 'temp';
    }
}