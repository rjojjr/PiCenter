import React from "react";
import { connect } from "react-redux";
import { loadSummaryThunk } from "../../actions/summary-actions";
import SummaryPage from "./SummaryPage";

const SummaryPageContainer = ({
  loadSummary,
  summary,
  user,
  isLoading,
  isError,
  errorMsg
}) => {

    let sensorIndex = 0;

    const selectSensor = (index) => {
        sensorIndex = index;
    }

  return (
    <div className={"container summaryPageContainer"}>
      {summary === [] &&
        !isLoading &&
        !isError &&
        user !== {} &&
        loadSummary(user)}
      {isLoading && <p>Loading summary...</p>}
      {summary !== [] && !isLoading && !isError && user !== {} && (
        <div className={"summaryHeader"}>
            {summary.map((sum, index) => {(
            <button key={index} className={"summarySensorSelector"} >{summary.roomName}</button>
        )})}

        </div>
      )}
    </div>
  );
};

const mapStateToProps = state => ({
  summary: state.summary,
  user: state.user,
  isLoading: state.isSummaryLoading,
  isError: state.isSummaryError,
  errorMsg: state.message
});

const mapDispatchToProps = {
  loadSummary: loadSummaryThunk
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SummaryPageContainer);
