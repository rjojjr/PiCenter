import {load, createUser} from "../services/axios-service";
import {setUser} from "./universal-actions";
import * as debugConstants from "../constants/debug-constants";
import {isLoading, isNotLoading, loadingError} from "./loader-actions";

export const USERS_IS_LOADING = 'USERS_IS_LOADING'
export const usersIsLoading = (loading) => ({
    type: USERS_IS_LOADING,
    loading
});

export const RESET_USER_SHOW_MSG = 'RESET_USER_SHOW_MSG'
export const resetUserShowMsg = () => ({
    type: RESET_USER_SHOW_MSG
});

export const USERS_SHOW_MSG = 'USERS_SHOW_MSG'
export const usersShowMsg = (msg) => ({
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

export const createUserThunk = (userName, firstName, lastName, password, admin) => async dispatch => {
    try {
        dispatch(usersIsLoading(true));
        const response = await createUser(userName, firstName, lastName, password, admin);
        if(response.data.responseBody.body === 'success'){
            usersShowMsg('User created successfully')
        }
        dispatch(isNotLoading());
        return
    } catch (error) {
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS){
            alert(error);
        }
        dispatch(loadingError('Network error'));
    }
};