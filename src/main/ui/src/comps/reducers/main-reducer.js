import {
    LOGGED_ON,
    NOT_LOGGED_ON,
    LOADING_ERROR,
    IS_LOADING
} from "../actions/loader-actions";

import{
    LOADING_PAGE,
    MSG_PAGE,
    LOGIN_PAGE,
    SUMMARY_PAGE
} from "../../constants/page-constants";

export const initialState = () => ({
    user: null,
    isLoading: true,
    isError: false,
    message: '',
    isShowMsg: false,
    errorMsg: '',
    currentPage: LOADING_PAGE
});

export default (state = initialState(), action = { type: undefined }) => {
    switch (action.type) {
        case IS_LOADING: {
            return {
                ...state,
                isLoading: true,
                isShowMsg: false,
                isError: false
            };
        };
        case LOGGED_ON: {
            return {
                ...state,
                user: action.user,
                isLoading: false,
                isShowMsg: false,
                isError: false,
                currentPage: action.user.page
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
                page: MSG_PAGE
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
                page: MSG_PAGE
            };
        };
        default:
            return state;
    }
}