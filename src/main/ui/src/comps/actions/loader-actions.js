import {load} from "../../services/axios-service";

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

export const loadAppThunk = teamName => async dispatch => {
    try {
        //dispatch(beginInitiatingSurvey());
        const initiationResponse = await load();
        const user = initiationResponse.data.user;
        user.userName !== null ? dispatch(loggedOn(user)) : dispatch(notLoggedOn(initiationResponse.data.responseBody.body));
    } catch (error) {
        dispatch(loadingError(error));
    }
};
