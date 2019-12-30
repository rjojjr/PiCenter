import React, {PureComponent} from 'react';
import {
    Scatter, ScatterChart, Line, XAxis, YAxis, ZAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

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
                <XAxis dataKey="time" name="time" tick={<CustomizedAxisTick/>} />
                <YAxis dataKey="inside" name="temp" unit="F" />
                <ZAxis dataKey="outside" range={[64, 144]} name="outside temp" unit="F" />
                <Tooltip cursor={{strokeDasharray: '3 3'}} />
                <Legend verticalAlign="top"/>
                <Scatter name="Office" data={[]} fill="#FFBE33" />
                <Scatter name="Living Room" data={[]} fill="#82ca9d" />
                <Scatter name="Bedroom" data={[]} fill="#8884d8" />
                <Scatter name="Server Room" data={[]} fill="#0c0707" />
                </ScatterChart>
                )}
            {(type === 'temp') && (
                <ScatterChart
                    width={800}
                    height={415}
                    margin={{
                        top: 5, right: 30, left: 20, bottom: 115,
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="time" name="time" tick={<CustomizedAxisTick/>}/>
                    <YAxis dataKey="inside" name="temp" unit="F"/>
                    <ZAxis dataKey="outside" range={[64, 144]} name="outside temp" unit="F"/>
                    <Tooltip cursor={{strokeDasharray: '3 3'}}/>
                    <Legend verticalAlign="top"/>
                    <Scatter name="Office" data={data[0].interval} fill="#FFBE33"/>
                    <Scatter name="Living Room" data={data[1].interval} fill="#82ca9d"/>
                    <Scatter name="Bedroom" data={data[2].interval} fill="#8884d8"/>
                    <Scatter name="Server Room" data={data[3].interval} fill="#0c0707"/>
                </ScatterChart>
            )}
            {(type === 'hum') && (
                <ScatterChart
                    width={800}
                    height={415}
                    margin={{
                        top: 5, right: 30, left: 20, bottom: 115,
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="time" name="time" tick={<CustomizedAxisTick/>}/>
                    <YAxis dataKey="inside" name="humidity" unit="%"/>
                    <ZAxis dataKey="outside" range={[64, 144]} name="outside humidity" unit="%"/>
                    <Tooltip cursor={{strokeDasharray: '3 3'}}/>
                    <Legend verticalAlign="top"/>
                    <Scatter name="Office" data={data[0].interval} fill="#FFBE33"/>
                    <Scatter name="Living Room" data={data[1].interval} fill="#82ca9d"/>
                    <Scatter name="Bedroom" data={data[2].interval} fill="#8884d8"/>
                    <Scatter name="Server Room" data={data[3].interval} fill="#0c0707"/>
                </ScatterChart>
            )}
        </div>
    );
};
export default ScatterPlotChart;

export const CustomizedAxisTick = ({x, y, stroke, payload}) => {
    return (
        <g transform={`translate(${x},${y})`}>
            <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-65)">{payload.value}</text>
        </g>
    );
}