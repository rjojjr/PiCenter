import React from "react";
import { connect } from "react-redux";
import { loadSummaryThunk } from "../../actions/summary-actions";
import SummaryPage from "./SummaryPage";
import SummaryPageHeader from "./SummaryPageHeader";

const SummaryPageContainer = ({ summary, user, isError, errorMsg }) => {
  let sensorIndex = 0;

  const selectSensor = index => {
    sensorIndex = index;
  };

  return (
    <div className={"container summaryPageContainer"}>
      <SummaryPageHeader summary={summary} selectSensor={selectSensor} />
      <SummaryPage summary={summary[sensorIndex]} />
    </div>
  );
};

export default SummaryPageContainer;
