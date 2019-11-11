import React, {useEffect} from "react";
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

  useEffect(() => {
    loadSummary();
  }, [loadSummary]);

  return (
    <div>
      {isLoading && <p>Loading summary...</p>}
      {isError && <p>`An error has happened: ${errorMsg}`</p>}
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
