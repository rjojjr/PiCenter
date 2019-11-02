import {
    LOGGED_ON,
    NOT_LOGGED_ON,
    LOADING_ERROR
} from "../actions/loader-actions";

export const initialState = () => ({
    user: null,
    loading: true,
    error: false,
    message: '',
    showMsg: false,
    errorMsg: ''
});

export default (state = initialState(), action = { type: undefined }) => {
    switch (action.type) {
        case LOGGED_ON: {
            return {
                ...state,
                user: action.user,
                loading: false,
                showMsg: false,
                error: false
            };
        };
        case NOT_LOGGED_ON: {
            return {
                ...state,
                user: null,
                loading: false,
                error: false,
                showMsg: true,
                message: action.msg
            };
        };
        case LOADING_ERROR: {
            return {
                ...state,
                user: null,
                loading: false,
                error: true,
                showMsg: false,
                errorMsg: action.msg
            };
        };
        default:
            return state;
    }
}