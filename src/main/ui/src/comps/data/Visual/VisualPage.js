import React from "react";

import GenericDatePicker from "../../global/GenericDatePicker";
import TempChart from "../../global/TempChart";
import Button from "react-bootstrap/Button";

const VisualPage = ({tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData, getChart}) => {

    return(
      <div className={"page visualDataPage"}>
              <GenericDatePicker currentDate={tempChartStart} changeDate={visualFromDate}>Start Date</GenericDatePicker>
              <GenericDatePicker currentDate={tempChartEnd} changeDate={visualToDate}>End Date</GenericDatePicker>
              <Button variant={"primary"} onClick={getChart}>
                  Get Chart
              </Button>
              <TempChart data={chartData} />
      </div>
    );

};
export default VisualPage