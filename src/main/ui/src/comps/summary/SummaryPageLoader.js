import React, { useEffect } from "react";
import { connect } from "react-redux";
import { loadSummaryThunk } from "../../actions/summary-actions";
import SummaryPageContainer from "./SummaryPageContainer";
import {logOff, changePage} from '../../actions/universal-actions';

const SummaryPageLoader = ({
  loadSummary,
  summary,
  user,
  isLoading,
  isError,
  errorMsg,
  canLoad,
    logOff,
    changePage
}) => {
  /*useEffect(() => {
    loadSummary(user);
  }, [loadSummary]);*/

  if (isLoading ){
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
        logOff={logOff}
        changePage={changePage}
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
  loadSummary: loadSummaryThunk,
  logOff,
  changePage
};

export default connect(mapStateToProps, mapDispatchToProps)(SummaryPageLoader);
