export const isSessionStorageAvailible = () =>{
        return typeof (sessionStorage) !== "undefined"
}

export const writeToken = (token) => {
    if(isSessionStorageAvailible()){
        function getCert() {
            sessionStorage.setItem("token", token);
        }
    }
}

export const readToken = () => {
    if(isSessionStorageAvailible()){
        return sessionStorage.getItem("token");
    }
    return undefined;
}