import React, {useState} from "react";


import {updateSession} from "../../services/axios-service";

import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";
import {
    isDataError,
    isDataLoading,
    getReadingsCSVThunk,
    isDownload,
    visualFromDate,
    visualToDate, getChartThunk, setChartType, setChartFlavor
} from "../../actions/data-actions";
import * as pageConstants from "../../constants/page-constants";
import CSVPageContainer from "./CSVPage/CSVPageContainer";
import VisualPageContainer from "./Visual/VisualPageContainer";

const DataPageContainer = ({user, changePage, isLoading, isError, errorMsg, isDataLoading, isDataError, logOff, getCSV, isDownload, isDownloadAvailable, tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData, scatData, getChart, setChartType, chartType, setChartFlavor, chartFlavor}) => {

    const tabClickHandler = (tabIndex) => {
        if (tabIndex === 0) {
            updateSession(pageConstants.CSV_EXPORT, user);
            changePage(pageConstants.CSV_EXPORT);
        }
        if (tabIndex === 1) {
            updateSession(pageConstants.DATA_VISUAL, user);
            changePage(pageConstants.DATA_VISUAL);
        }
    }

    return (
        <div className="pageContainer dataPageContainer">
            {user.page === pageConstants.DATA && (
                <CSVPageContainer user={user} changePage={changePage} updateSession={updateSession}
                                  isLoading={isLoading} isError={isError} errorMsg={errorMsg}
                                  isDataLoading={isDataLoading} isDataError={isDataError} logOff={logOff}
                                  onClickHandler={tabClickHandler} getCSV={getCSV} isDownload={isDownload}
                                  isDownloadAvailable={isDownloadAvailable}/>
            )}
            {user.page === pageConstants.CSV_EXPORT && (
                <CSVPageContainer user={user} changePage={changePage} updateSession={updateSession}
                                  isLoading={isLoading} isError={isError} errorMsg={errorMsg}
                                  isDataLoading={isDataLoading} isDataError={isDataError} logOff={logOff}
                                  onClickHandler={tabClickHandler} getCSV={getCSV} isDownload={isDownload}
                                  isDownloadAvailable={isDownloadAvailable}/>
            )}
            {user.page === pageConstants.DATA_VISUAL && (
                <VisualPageContainer user={user} changePage={changePage} updateSession={updateSession}
                                     isLoading={isLoading} isError={isError} errorMsg={errorMsg}
                                     isDataLoading={isDataLoading} isDataError={isDataError} logOff={logOff}
                                     onClickHandler={tabClickHandler} tempChartStart={tempChartStart}
                                     tempChartEnd={tempChartEnd} visualFromDate={visualFromDate}
                                     visualToDate={visualToDate} chartData={chartData} getChart={getChart} scatData={scatData}
                                        setChartType={setChartType} chartType={chartType} setChartFlavor={setChartType} chartFlavor={chartFlavor}/>
            )}
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
    tempChartEnd: state.tempChartEnd,
    chartData: state.chartData,
    chartType: state.chartType,
    chartFlavor: state.chartFlavor,
    scatData: state.scatData
});

const mapDispatchToProps = {
    logOff,
    changePage,
    isDataLoading,
    isDataError,
    getCSV: getReadingsCSVThunk,
    isDownload,
    visualFromDate,
    visualToDate,
    getChart: getChartThunk,
    setChartType: setChartType,
    setChartFlavor: setChartFlavor
};

export default connect(mapStateToProps, mapDispatchToProps)(DataPageContainer);