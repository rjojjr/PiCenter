import React from "react";

import GenericDatePicker from "../../global/GenericDatePicker";

const VisualPage = ({tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData}) => {

    return(
      <div className={"page visualDataPage"}>
          <div className={"visualDateContainer"}>
              <GenericDatePicker currentDate={tempChartStart} changeDate={visualFromDate}>Start Date</GenericDatePicker>
              <GenericDatePicker currentDate={tempChartEnd} changeDate={visualToDate}>End Date</GenericDatePicker>
          </div>
      </div>
    );

};
export default VisualPage