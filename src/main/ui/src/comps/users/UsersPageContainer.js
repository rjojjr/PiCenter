import React, {useEffect} from "react";
import {connect} from "react-redux";

import {logOff, changePage} from '../../actions/universal-actions';

import {usersIsLoading, usersLoadingError, resetUserShowMsg, createUserThunk} from "../../actions/user-actions";

import {updateSession} from "../../services/axios-service";

import * as pageConstants from "../../constants/page-constants";

import CreateUserContainer from "./create-user/CreateUserContainer";

const UsersPageContainer = ({
                               user, changePage,
                               isLoading, isError,
    createUser,
                               errorMsg, logOff, usersIsLoading, usersLoadingError, userMsg,
    resetUserShowMsg,
                           }) => {

    const tabClickHandler = (tabIndex) => {
        switch (tabIndex) {
            case 0: {
                changePage(pageConstants.CREATE_USER);
                updateSession(pageConstants.CREATE_USER, user);
            }
                ;
            default: {
                changePage(pageConstants.USERS);
                updateSession(pageConstants.USERS, user);
            }
        }
    }
    updateSession(user.page, user);
    return (
        //User if selection when more pages added.
        <div className={"pageContainer usersPageContainer"}>
            <CreateUserContainer changePage={changePage} isLoading={isLoading} logOff={logOff} user={user} onClickHandler={tabClickHandler}
                                 resetUserMsg={resetUserShowMsg} createUser={createUser} userMsg={userMsg}/>
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
    resetUserShowMsg,
    createUser: createUserThunk
};

export default connect(mapStateToProps, mapDispatchToProps)(UsersPageContainer);

