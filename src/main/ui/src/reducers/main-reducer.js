import {
    LOGGED_ON,
    NOT_LOGGED_ON,
    LOADING_ERROR,
    IS_LOADING
} from "../actions/loader-actions";

import {
    IS_LOGGED_ON,
    IS_LOGGING_ON,
    LOGON_ERROR
} from "../actions/logon-actions";

import {
    RESET_IS_ERROR,
    RESET_IS_SHOW_MSG
} from "../actions/universal-actions";


export const initialState = () => ({
    user: {},
    isLoading: true,
    isError: false,
    message: '',
    isShowMsg: false,
    errorMsg: '',
    isLoggedOn: false,
    isLoggingOn: false
});

export default (state = initialState(), action = {type: undefined}) => {
    switch (action.type) {
        case RESET_IS_ERROR: {
            return {
                ...state,
                isError: false,
                errorMsg: ''
            };
        };
        case RESET_IS_SHOW_MSG: {
            return {
                ...state,
                isShowMsg: false,
                message: ''
            };
        };
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
        }
            ;
        case LOADING_ERROR: {
            return {
                ...state,
                user: null,
                isLoading: false,
                isError: true,
                errorMsg: action.msg,
                isLoggedOn: false
            };
        };
        case LOGON_ERROR: {
            return {
                ...state,
                user: null,
                isLoggingOn: false,
                isShowMsg: true,
                message: action.msg,
                isLoggedOn: false
            };
        };
        case IS_LOGGING_ON: {
            return {
                ...state,
                isLoggingOn: true,
                isShowMsg: false,
                isError: false,
                isLoggedOn: false
            };
        };
        case LOGGED_ON: {
            return {
                ...state,
                isLoggingOn: false,
                user: action.user,
                isLoggedOn: true
            };
        };
        default:
            return state;
    }
}