import {load} from "../services/axios-service";
import {isLoggedOn} from "./logon-actions";
import * as debugConstants from '../constants/debug-constants'
import {setUser} from "./universal-actions";

export const IS_LOADING = 'IS_LOADING'
export const isLoading = () => ({
    type: IS_LOADING
});

export const IS_NOT_LOADING = 'IS_NOT_LOADING'
export const isNotLoading = () => ({
    type: IS_NOT_LOADING
});

export const LOGGED_ON = 'LOGGED_ON'
export const loggedOn = user => ({
    type: LOGGED_ON,
    user
});

export const NOT_LOGGED_ON = 'NOT_LOGGED_ON'
export const notLoggedOn = msg => ({
    type: NOT_LOGGED_ON,
    msg
});

export const LOADING_ERROR = 'LOADING_ERROR'
export const loadingError = msg => ({
    type: LOADING_ERROR,
    msg
});

export const loadAppThunk = () => async dispatch => {
    try {
        dispatch(isLoading());
        const initiationResponse = await load();
        const user = initiationResponse.data.restUser;

        dispatch(setUser(user));
        dispatch(isNotLoading());
        return
    } catch (error) {
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS){
            alert(error);
        }
        dispatch(loadingError('Network error'));
    }
};
