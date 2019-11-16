import {
    LOGGED_ON,
    NOT_LOGGED_ON,
    LOADING_ERROR,
    IS_LOADING, IS_NOT_LOADING
} from "../actions/loader-actions";

import {
    IS_LOGGED_ON,
    IS_LOGGING_ON,
    LOGON_ERROR
} from "../actions/logon-actions";

import {
    SUMMARY_LOADING,
    SET_SUMMARY,
    SUMMARY_LOADING_ERROR, SUMMARY_DONE_LOADING, SUMMARY_CAN_RENDER
} from "../actions/summary-actions";

import {
    CHANGE_PAGE,
    LOGOUT,
    RESET_IS_ERROR,
    RESET_IS_SHOW_MSG,
    SET_USER
} from "../actions/universal-actions";
import {USERS_IS_LOADING, USERS_LOADING_ERROR} from "../actions/user-actions";


export const initialState = () => ({
    user: {},
    isLoadingError: false,
    loadingErrorMsg: '',
    isLoading: false,
    isError: false,
    message: '',
    isShowMsg: false,
    errorMsg: '',
    isLoggedOn: false,
    isLoggingOn: false,
    summary: [],
    isSummaryLoading: true,
    isSummaryError: false,
    isUserLoading: true,
    isUserError: false,
    loaded: false,
    canRenderSummary: false
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
                user: {},
                isLoading: true,
                isShowMsg: false,
                isError: false,
                isLoggedOn: false,
                loaded: true
            };
        };
        case IS_NOT_LOADING: {
            return {
                ...state,
                isLoading: false,
                isShowMsg: false,
                isError: false,
                loaded: true
            };
        };;
        case NOT_LOGGED_ON: {
            return {
                ...state,
                user: {},
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
                user: {},
                isLoading: false,
                isLoadingError: true,
                loadingErrorMsg: action.msg,
                isLoggedOn: false
            };
        };
        case LOGON_ERROR: {
            return {
                ...state,
                user: {},
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
                user: action.user,
                isLoggedOn: true
            };
        };
        case SET_USER: {
            const loggedOn = () => {
                if(action.user.userName !== 'null') {
                    return true;
                }
                return false;
            }
            return {
                ...state,
                user: action.user,
                isLoggedOn: loggedOn()
            };
        };
        case SET_SUMMARY: {
            return {
                ...state,
                summary: action.summary,
                isSummaryError: false
            };
        };
        case SUMMARY_CAN_RENDER: {
            return {
                ...state,
                canRenderSummary: action.canRender
            };
        };
        case SUMMARY_LOADING_ERROR: {
            return {
                ...state,
                isSummaryError: true,
                isSummaryLoading: false,
                isShowMsg: true,
                message: action.msg
            };
        };
        case SUMMARY_LOADING: {
            return {
                ...state,
                isSummaryError: false,
                isSummaryLoading: true,
                isShowMsg: false,
                canRenderSummary: false
            };
        };
        case SUMMARY_DONE_LOADING: {
            return {
                ...state,
                isSummaryError: false,
                isSummaryLoading: false,
                isShowMsg: false
            };
        };
        case CHANGE_PAGE: {
            const newUser = {...state.user};
            newUser.page = action.page;
            return {
                ...state,
                user: newUser
            };
        };
        case LOGOUT: {
            return {
                ...state,
                user: action.user,
                isLoadingError: false,
                loadingErrorMsg: '',
                isLoading: false,
                isError: false,
                message: '',
                isShowMsg: false,
                errorMsg: '',
                isLoggedOn: false,
                isLoggingOn: false,
                summary: [],
                isSummaryLoading: true,
                isSummaryError: false,
                loaded: false,
                canRenderSummary: false
            };
        };
        case CHANGE_PAGE: {
            const newUser = {...state.user};
            newUser.page = action.page;
            return {
                ...state,
                user: newUser
            };
        };
        case USERS_IS_LOADING: {
            return {
                ...state,
                isUserLoading: action.loading
            };
        };
        case USERS_LOADING_ERROR: {
            return {
                ...state,
                isUserError: action.error,
                errorMsg: action.msg
            };
        };
        default:
            return state;
    }
}