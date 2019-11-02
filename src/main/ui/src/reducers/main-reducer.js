import {
    LOGGED_ON,
    NOT_LOGGED_ON,
    LOADING_ERROR,
    IS_LOADING
} from "../actions/loader-actions";



export const initialState = () => ({
    user: null,
    isLoading: true,
    isError: false,
    message: '',
    isShowMsg: false,
    errorMsg: '',
    isLoggedOn: false
});

export default (state = initialState(), action = { type: undefined }) => {
    switch (action.type) {
        case IS_LOADING: {
            return {
                ...state,
                isLoading: true,
                isShowMsg: false,
                isError: false,
                isLoggedOn: false
            };
        };
        case LOGGED_ON: {
            return {
                ...state,
                user: action.user,
                isLoading: false,
                isShowMsg: false,
                isError: false,
                isLoggedOn: true
            };
        };
        case NOT_LOGGED_ON: {
            return {
                ...state,
                user: null,
                isLoading: false,
                isError: false,
                isShowMsg: true,
                message: action.msg,
                isLoggedOn: false
            };
        };
        case LOADING_ERROR: {
            return {
                ...state,
                user: null,
                isLoading: false,
                isError: true,
                isShowMsg: false,
                errorMsg: action.msg,
                isLoggedOn: false
            };
        };
        default:
            return state;
    }
}