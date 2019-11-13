import React, { useEffect } from "react";
import { connect } from "react-redux";
import { loadSummaryThunk } from "../../actions/summary-actions";
import SummaryPageContainer from "./SummaryPageContainer";

const SummaryPageLoader = ({
  loadSummary,
  summary,
  user,
  isLoading,
  isError,
  errorMsg,
  canLoad
}) => {
  /*useEffect(() => {
    loadSummary(user);
  }, [loadSummary]);*/

  if (summary.length === 0 && !isLoading) {
    loadSummary(user);
  }

  return (
    <div>
      {isError && <p>An error has happened: {errorMsg}</p>}
      <SummaryPageContainer
        summary={summary}
        user={user}
        isLoading={isLoading}
        canLoad={canLoad}
      />
    </div>
  );
};

const mapStateToProps = state => ({
  summary: state.summary,
  user: state.user,
  isLoading: state.isSummaryLoading,
  isError: state.isSummaryError,
  errorMsg: state.message,
  canLoad: state.canRenderSummary
});

const mapDispatchToProps = {
  loadSummary: loadSummaryThunk
};

export default connect(mapStateToProps, mapDispatchToProps)(SummaryPageLoader);
