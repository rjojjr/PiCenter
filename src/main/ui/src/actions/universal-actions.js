import {logOut} from "../services/axios-service";

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
    await logOut();
    dispatch(logout());
}

export const LOGOUT = 'LOGOUT'
export const logout = () => ({
    type: LOGOUT
});