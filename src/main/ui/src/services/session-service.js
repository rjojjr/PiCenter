export const isSessionStorageAvailible = () =>{
        return typeof (sessionStorage) !== "undefined"
}

export const writeToken = (token) => {
    if(isSessionStorageAvailible()){
            sessionStorage.setItem("token", token);
            return true;
    }
    return false;
}

export const readToken = () => {
    if(isSessionStorageAvailible()){
        const token = sessionStorage.getItem("token");
        if(token === null){
            return undefined;
        }
        return token;
    }
    return undefined;
}