import {logOn} from "../services/axios-service";

export const LOGON_ERROR = 'LOGON_ERROR'
export const logonError = msg => ({
    type: LOGON_ERROR,
    msg
});

export const IS_LOGGING_ON = 'LOGGING_ON'
export const isLoggingOn = () => ({
    type: IS_LOGGING_ON
});

export const IS_LOGGED_ON = 'LOGGED_ON'
export const isLoggedOn = (user) => ({
    type: IS_LOGGED_ON,
    user: user
});

export const logOnThunk = (username, password) => async dispatch => {
    try {
        dispatch(isLoggingOn());
        const response = await logOn(username, password);
        const user = response.data.user;
        user.userName !== null ? dispatch(isLoggedOn(user)) : dispatch(logonError(response.data.responseBody.body));
    } catch (error) {
        dispatch(logonError('Try again later'));
    }
};
