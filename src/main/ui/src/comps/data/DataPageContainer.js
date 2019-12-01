import React, {useState} from "react";

import LoadingView from "../global/LoadingView";

import {updateSession} from "../../services/axios-service";

import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";
import {isDataError, isDataLoading, getReadingsCSVThunk} from "../../actions/data-actions";
import * as pageConstants from "../../constants/page-constants";
import CSVPageContainer from "./CSVPage/CSVPageContainer";

const DataPageContainer = ({user, changePage, isLoading, isError, errorMsg, isDataLoading, isDataError, logOff, getCSV}) => {

    const tabClickHandler = (tabIndex) => {
        switch (tabIndex) {
            case 0: {
                changePage(pageConstants.CSV_EXPORT);
                updateSession(pageConstants.CSV_EXPORT, user);
            }
                ;
            default: {
                changePage(pageConstants.DATA);
                updateSession(pageConstants.DATA, user);
            }
        }
    }
  
    updateSession(user.page, user);
    
    return(
        <div className="pageContainer dataPageContainer">
            <CSVPageContainer user={user} changePage={changePage} updateSession={updateSession} isLoading={isLoading} isError={isError} errorMsg={errorMsg} isDataLoading={isDataLoading} isDataError={isDataError} logOff={logOff} onClickHandler={tabClickHandler} getCSV={getCSV}/>
        </div>    
    )
    
};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isDataLoading,
    isError: state.isDataError,
    errorMsg: state.dataMsg
});

const mapDispatchToProps = {
    logOff,
    changePage,
    isDataLoading,
    isDataError,
    getCSV: getReadingsCSVThunk
};

export default connect(mapStateToProps, mapDispatchToProps)(DataPageContainer);