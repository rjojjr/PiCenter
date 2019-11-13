import {logOut} from "../services/axios-service";
import {isLoading} from "./loader-actions";

export const RESET_IS_SHOW_MSG = 'RESET_IS_SHOW_MSG'
export const resetIsShowMsg = () => ({
    type: RESET_IS_SHOW_MSG
});

export const RESET_IS_ERROR = 'RESET_IS_ERROR'
export const resetIsError = () => ({
    type: RESET_IS_ERROR
});

export const SET_USER = 'SET_USER'
export const setUser = (user, loggedOn) => ({
    type: SET_USER,
    user,
    loggedOn
});

export const logOff = () => async dispatch => {
    try{
    const response = await logOut();
        const user = response.data.restUser;
    dispatch(logout(user));
    }catch (e) {
        
    }
}

export const LOGOUT = 'LOGOUT'
export const logout = (user) => ({
    type: LOGOUT,
    user
});