import {load, createUser from "../services/axios-service";
import {setUser} from "./universal-actions";
import * as debugConstants from "../constants/debug-constants";
import {isLoading, isNotLoading, loadingError} from "./loader-actions";

export const USERS_IS_LOADING = 'USERS_IS_LOADING'
export const usersIsLoading = (loading) => ({
    type: USERS_IS_LOADING,
    loading
});

export const RESET_SHOW_MSG = 'RESET_SHOW_MSG'
export const resetSHowMsg = () => ({
    type: RESET_SHOW_MSG
});

export const USERS_SHOW_MSG = 'USERS_SHOW_MSG'
export const sHowMsg = (msg) => ({
    type: USERS_SHOW_MSG
});

export const USERS_DONE_LOADING = 'USERS_DONE_LOADING'
export const usersDoneLoading = () => ({
    type: USERS_DONE_LOADING
});

export const USERS_LOADING_ERROR = 'USERS_LOADING_ERROR'
export const usersLoadingError = (msg, error) => ({
    type: USERS_LOADING_ERROR,
    msg, error
});

export const createUserThunk = () => async dispatch => {
    try {
        dispatch(usersIsLoading(true));
        const initiationResponse = await createUser();

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