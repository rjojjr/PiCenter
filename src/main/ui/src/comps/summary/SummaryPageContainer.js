import React, { useState } from "react";
import SummaryPage from "./SummaryPage";
import SummaryPageHeader from "./SummaryPageHeader";
import LoadingView from "../global/LoadingView";

const SummaryPageContainer = ({
  summary,
  user,
  isError,
  errorMsg,
  isLoading,
  canLoad
}) => {
  const [sensorIndex, setSensorIndex] = useState(0);

  const selectSensor = index => {
    setSensorIndex(index);
  };

  const showSummary = () => {
    if (summary.length === 0) {
      return "";
    } else {
      return summary[sensorIndex];
    }
  };

  return (
    <div className={"container summaryPageContainer"}>
      <LoadingView isLoading={isLoading} message={"Loading.."} />
      {canLoad &&
        !isLoading && (
          <div>
            <SummaryPageHeader
              isLoading={isLoading}
              summary={summary}
              selectSensor={selectSensor}
            />
            <SummaryPage canRender={canLoad} isLoading={isLoading} summary={showSummary()} />
          </div>
        )}
    </div>
  );
};

export default SummaryPageContainer;
