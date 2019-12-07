import React, {useState} from "react";

import LoadingView from "../global/LoadingView";

import {updateSession} from "../../services/axios-service";

import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";
import {
    isDataError,
    isDataLoading,
    getReadingsCSVThunk,
    isDownload,
    visualFromDate,
    visualToDate
} from "../../actions/data-actions";
import * as pageConstants from "../../constants/page-constants";
import CSVPageContainer from "./CSVPage/CSVPageContainer";

const DataPageContainer = ({user, changePage, isLoading, isError, errorMsg, isDataLoading, isDataError, logOff, getCSV, isDownload, isDownloadAvailable, tempChartStart, tempChartEnd, visualFromDate, visualToDate}) => {

    const tabClickHandler = (tabIndex) => {
        switch (tabIndex) {
            case 0: {
                changePage(pageConstants.CSV_EXPORT);
                updateSession(pageConstants.CSV_EXPORT, user);
            };
            case 1: {
                changePage(pageConstants.DATA_VISUAL);
                updateSession(pageConstants.DATA_VISUALT, user);
            };
            default: {
                changePage(pageConstants.DATA);
                updateSession(pageConstants.DATA, user);
            }
        }
    }
  
    updateSession(user.page, user);
    
    return(
        <div className="pageContainer dataPageContainer">
            <CSVPageContainer user={user} changePage={changePage} updateSession={updateSession} isLoading={isLoading} isError={isError} errorMsg={errorMsg} isDataLoading={isDataLoading} isDataError={isDataError} logOff={logOff} onClickHandler={tabClickHandler} getCSV={getCSV} isDownload={isDownload} isDownloadAvailable={isDownloadAvailable}/>
        </div>    
    )
    
};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isDataLoading,
    isError: state.isDataError,
    errorMsg: state.dataMsg,
    isDownloadAvailable: state.isDownloadAvailable,
    tempChartStart: state.tempChartStart,
    tempChartEnd: state.tempChartEnd
});

const mapDispatchToProps = {
    logOff,
    changePage,
    isDataLoading,
    isDataError,
    getCSV: getReadingsCSVThunk,
    isDownload,
    visualFromDate,
    visualToDate
};

export default connect(mapStateToProps, mapDispatchToProps)(DataPageContainer);