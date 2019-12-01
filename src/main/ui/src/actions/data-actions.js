import {getReadingsCSV} from "../services/axios-service";
import {setUser} from "./universal-actions";
import * as debugConstants from "../constants/debug-constants";
import {isLoading, isNotLoading, loadingError} from "./loader-actions";

export const IS_DATA_LOADING = 'IS_DATA_LOADING'
export const isDataLoading = (loading) => ({
    type: IS_DATA_LOADING,
    loading
});

export const IS_DATA_ERROR = 'IS_DATA_ERROR'
export const isDataError = (msg) => ({
    type: IS_DATA_ERROR,
    msg
});

export const getReadingsCSVThunk = (user) => async dispatch => {
    try {
        dispatch(isDataLoading(true));
        const response = await getReadingsCSV(user);
        dispatch(isDataLoading(false));
        return
    } catch (error) {
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS){
            alert(error);
        }
        dispatch(loadingError('Network error'));
    }
};
