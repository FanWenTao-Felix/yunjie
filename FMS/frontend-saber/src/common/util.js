const copyObject = (data) => {
  return JSON.parse(JSON.stringify(data));
}

const arrayToString = (data) => {
  let str = "";
  for (let i = 0; i < data.length; i++) {
    str += data[i];
    if (i >= data.length - 1) break;
    str += ","
  }
  return str;
}
export {
  copyObject,
  arrayToString,
}