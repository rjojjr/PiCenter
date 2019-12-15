import React, {useState} from "react";

import GenericDatePicker from "../../global/GenericDatePicker";
import TempChart from "../../global/TempChart";

import Button from "react-bootstrap/Button";

import Select from 'react-select';
import {CHART_TYPES} from "../../../constants/page-constants";
import DiffChart from "../../global/DiffChart";

const VisualPage = ({tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData, getChart, setChartType, chartType, setChartFlavor, chartFlavor, intSelected}) => {

    const [typeSelected, setTypeSelected] = useState(intSelected);
    const [flavor, setFlavor] = useState(chartFlavor);

    const retrieveChart = () => {
        getChart(chartType);
    }

    const handleChange = (selected) => {
        if (selected.value === typeSelected) {
            return;
        }
        setTypeSelected(selected.value);
        if (selected.value > 2) {
            setFlavor('hl');
        } else {
            setFlavor('avg');
        }
        if (selected.value === 1 || selected.value === 3) {
            setChartType('temp');
        } else {
            setChartType('hum');
        }
        setChartFlavor(flavor);
    }

    const getChartLabel = () => {
        return CHART_TYPES[typeSelected - 1].label + ' Chart';
    }

    return (
        <div className={"page visualDataPage"}>
            <div className={"dateSelectorContainer headerButtonContainer"}>

                <p>
                    <h4><b>{getChartLabel()}</b></h4>
                    <Select options={CHART_TYPES} onChange={handleChange} value={typeSelected}
                            placeholder={'Chart Type'}/>
                    <GenericDatePicker currentDate={tempChartStart} changeDate={visualFromDate}>Start
                        Date</GenericDatePicker>
                    <GenericDatePicker currentDate={tempChartEnd} changeDate={visualToDate}>End Date</GenericDatePicker>
                    <Button variant={"primary"} onClick={retrieveChart}>
                        Get Chart
                    </Button>

                </p>
            </div>

            {typeSelected < 3 && (
                <TempChart data={chartData}/>
            )}

            {typeSelected > 2 && (
                <DiffChart data={chartData}/>
            )}

        </div>
    );

};
export default VisualPage