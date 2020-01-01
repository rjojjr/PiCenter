import React, {useState} from 'react';
import {
    Scatter, ScatterChart, Line, XAxis, YAxis, ZAxis, CartesianGrid, Tooltip, Legend, Brush
} from 'recharts';
import moment from 'moment'

const ScatterPlotChart = ({data, type}) => {

    return (
        <div className={"scatterChart"}>
            {(data === []) && (
                <ScatterChart
                width={800}
                height={415}
                margin={{
                top: 5, right: 30, left: 20, bottom: 115,
            }}
                >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" name="time" tick={<CustomizedAxisTick/>} tickFormatter = {(unixTime) => moment(unixTime).format('MM/dd/yyyy HH:mm Do')} type = 'number' />
                <YAxis dataKey="inside" name="temp" unit="F" />
                <ZAxis dataKey="outside" name="outside temp" unit="F" />
                <Tooltip cursor={{strokeDasharray: '3 3'}} />
                <Legend verticalAlign="top"/>
                <Scatter name="Office" data={[]} fill="#FFBE33" />
                <Scatter name="Living Room" data={[]} fill="#82ca9d" />
                <Scatter name="Bedroom" data={[]} fill="#8884d8" />
                <Scatter name="Server Room" data={[]} fill="#0c0707" />
                </ScatterChart>
                )}
            {(type === 'temp') && (
                <div className={"scrollChart"}>
                <ScatterChart
                    width={800}
                    height={415}
                    margin={{
                        top: 5, right: 30, left: 20, bottom: 115,
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="time"
                           name="time"
                           tick={<CustomizedAxisTick/>}
                           scale='time'/>
                    <YAxis dataKey="inside" name="temp" unit="F"/>
                    <ZAxis dataKey="outside"  name="outside temp" unit="F"/>
                    <Tooltip cursor={{strokeDasharray: '3 3'}}/>
                    <Legend verticalAlign="top" align="left"/>
                    <Scatter name="Office" data={data[0].interval} fill="#FFBE33"/>
                    <Scatter name="Living Room" data={data[1].interval} fill="#82ca9d"/>
                    <Scatter name="Bedroom" data={data[2].interval} fill="#8884d8"/>
                    <Scatter name="Server Room" data={data[3].interval} fill="#0c0707"/>
                </ScatterChart>
                </div>
            )}
            {(type === 'hum') && (
                <div className={"scrollChart"}>
                <ScatterChart
                    width={800}
                    height={415}
                    margin={{
                        top: 5, right: 30, left: 20, bottom: 115,
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="time"
                           name="time"
                           tick={<CustomizedAxisTick/>}
                           tickFormatter = {convertDate}
                           scale='time'/>
                    <YAxis dataKey="inside" name="humidity" unit="%"/>
                    <ZAxis dataKey="outside"  name="outside humidity" unit="%"/>
                    <Tooltip cursor={{strokeDasharray: '3 3'}}/>
                    <Legend verticalAlign="top"/>
                    <Scatter name="Office" data={data[0].interval} fill="#FFBE33"/>
                    <Scatter name="Living Room" data={data[1].interval} fill="#82ca9d"/>
                    <Scatter name="Bedroom" data={data[2].interval} fill="#8884d8"/>
                    <Scatter name="Server Room" data={data[3].interval} fill="#0c0707"/>
                </ScatterChart>
                </div>
            )}
        </div>
    );
};
export default ScatterPlotChart;

const convertDate = (tickItem) => {
    const date = new Date(tickItem);
    return `${(date.getMonth() + 1)}/${date.getDate()}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`;
}

export const CustomizedAxisTick = ({x, y, stroke, payload}) => {
    return (
        <g transform={`translate(${x},${y})`}>
            <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-65)">{convertDate(payload.value)}</text>
        </g>
    );
}