import React from "react";
import { connect } from "react-redux";
import { loadSummaryThunk } from "../../actions/summary-actions";
import SummaryPageContainer from "./SummaryPageContainer";

const SummaryPageLoader = ({
  loadSummary,
  summary,
  user,
  isLoading,
  isError,
  errorMsg
}) => {
  /*let sensorIndex = 0;

  const selectSensor = index => {
    sensorIndex = index;
  };*/

  return (
    <div className={"container summaryPageContainer"}>
      {summary === [] &&
        !isLoading &&
        !isError &&
        user !== {} &&
        loadSummary(user) && <p>Loading summary...</p>}
      {isLoading && <p>Loading summary...</p>}
      {summary !== [] && !isLoading && !isError && user !== {} && (
        <SummaryPageContainer summary={summary} user={user} />
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
)(SummaryPageLoader);
