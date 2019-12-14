import React, {useState} from "react";

import GenericDatePicker from "../../global/GenericDatePicker";
import TempChart from "../../global/TempChart";

import Button from "react-bootstrap/Button";

import Select from 'react-select';
import {CHART_TYPES} from "../../../constants/page-constants";

const VisualPage = ({tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData, getChart, setChartType, chartType, intSelected}) => {

    const [typeSelected, setTypeSelected] = useState(intSelected);



    const retrieveChart = () => {
        getChart(chartType);
    }

    const handleChange = (selected) => {
        setTypeSelected(selected.value);
        if (selected.value === 1){
            setChartType('temp');
        }else{
            setChartType('hum');
        }
    }

    const getChartLabel = () => {
        return CHART_TYPES[typeSelected - 1].label + ' Chart';
    }

    return(
      <div className={"page visualDataPage"}>
          <div className={"dateSelectorContainer headerButtonContainer"}>

                  <p>
                      <h4><b>{getChartLabel()}</b></h4>
                      <Select options={CHART_TYPES} onChange={handleChange} value={typeSelected} placeholder={'Chart Type'}/>
                      <GenericDatePicker currentDate={tempChartStart} changeDate={visualFromDate}>Start Date</GenericDatePicker>
                      <GenericDatePicker currentDate={tempChartEnd} changeDate={visualToDate}>End Date</GenericDatePicker>
                      <Button variant={"primary"} onClick={retrieveChart}>
                          Get Chart
                      </Button>

                  </p>
          </div>



              <TempChart data={chartData} />
      </div>
    );

};
export default VisualPage