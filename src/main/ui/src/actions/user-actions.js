export const USERS_IS_LOADING = 'USERS_IS_LOADING'
export const usersIsLoading = (loading) => ({
    type: USERS_IS_LOADING,
    loading
});

export const USERS_DONE_LOADING = 'USERS_DONE_LOADING'
export const usersDoneLoading = () => ({
    type: USERS_DONE_LOADING
});

export const USERS_LOADING_ERROR = 'USERS_LOADING_ERROR'
export const usersLoadingError = (msg) => ({
    type: USERS_LOADING_ERROR,
    msg
});