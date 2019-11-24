import React, {useEffect} from "react";
import {connect} from "react-redux";

import {logOff, changePage} from '../../actions/universal-actions';

import {usersIsLoading, usersLoadingError, resetUserSHowMsg, createUserThunk} from "../../actions/user-actions";
import CreateUserPage from "./create-user/CreateUserPage";

import {createUser, updateSession} from "../../services/axios-service";

import * as pageConstants from "../../constants/page-constants";
import CreateUserContainer from "./create-user/CreateUserContainer";

const UserPageContainer = ({
                               user,
                               isLoading, isError,
                               errorMsg, logOff, usersIsLoading, usersLoadingError, userMsg, changePage
                           }) => {

    const tabClickHandler = (tabIndex) => {
        switch (tabIndex) {
            case 0: {
                changePage(pageConstants.CREATE_USER);

            };
            default: {
                changePage(pageConstants.USERS);
                updateSession(pageConstants.USERS, user);
            }
        }
    }
    updateSession(user.page, user);
    return (
        <div>
            <CreateUserContainer changePage={changePage} logOff={logOff} user={user} onClickHandler={tabClickHandler} resetUserMsg={resetUserSHowMsg} createUser={createUser} userMsg={userMsg}/>
        </div>
    );
};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isUserLoading,
    isError: state.isUserError,
    errorMsg: state.message,
    userMsg: state.userMsg
});

const mapDispatchToProps = {
    logOff,
    usersIsLoading,
    usersLoadingError,
    changePage,
    resetUserSHowMsg,
    createUser: createUserThunk
};

export default connect(mapStateToProps, mapDispatchToProps)(UserPageContainer);

