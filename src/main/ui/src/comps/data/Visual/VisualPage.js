import React, {useState} from "react";

import GenericDatePicker from "../../global/GenericDatePicker";

import TempChart from "../../global/TempChart";
import DiffChart from "../../global/DiffChart";
import ScatterPlotChart from "../../global/ScatterPlotChart";

import Button from "react-bootstrap/Button";

import Select from 'react-select';
import {CHART_TYPES} from "../../../constants/page-constants";

const VisualPage = ({tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData, scatData, getChart, setChartType, chartType, setChartFlavor, chartFlavor, intSelected}) => {

    const [typeSelected, setTypeSelected] = useState(intSelected);
    const [flavor, setFlavor] = useState(chartFlavor);

    const retrieveChart = () => {
        getChart(chartType, chartFlavor);
    }

    const handleChange = (selected) => {
        if (selected.value === typeSelected) {
            return;
        }
        console.log(selected.value);
        setTypeSelected(selected.value);
        /*if (selected.value > 2) {
            setFlavor('hl');
        } else {
            setFlavor('avg');
        }*/
        if (selected.value === 1 || selected.value === 3) {
            if (selected.value > 2) {
                setFlavor('hl');
                setChartType('temp', 'hl');
            } else {
                setFlavor('avg');
                setChartType('temp', 'avg');
            }
        } else if (selected.value === 5) {
            setFlavor('scat');
            setChartType('temp', 'scat');
        } else if (selected.value === 6) {
            setFlavor('scat');
            setChartType('hum', 'scat');
        } else {
            if (selected.value > 2) {
                setFlavor('hl');
                setChartType('hum', 'hl');
            } else {
                setFlavor('avg');
                setChartType('hum', 'avg');
            }
        }

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
                            placeholder={'Chart Type'}></Select>
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

            {typeSelected > 2 && typeSelected < 5 && (
                <DiffChart data={chartData}/>
            )}
            {typeSelected === 5 && (
                <ScatterPlotChart data={scatData} type={'temp'}/>
            )}
            {typeSelected === 6 && (
                <ScatterPlotChart data={scatData} type={'hum'}/>
            )}
        </div>
    );

};
export default VisualPage