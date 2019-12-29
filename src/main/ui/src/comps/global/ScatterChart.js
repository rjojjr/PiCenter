import React, {PureComponent} from 'react';
import {
    Scatter, Line, XAxis, YAxis, ZAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const ScatterChart = ({data, type}) => {

    return (
        <div className={"scatterChart"}>
        {(type === 'temp') && (
            <ScatterChart width={730} height={250}
                          margin={{ top: 20, right: 20, bottom: 10, left: 10 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" name="time" />
                <YAxis dataKey="inside" name="temp" unit="F" />
                <ZAxis dataKey="outside" range={[64, 144]} name="outside temp" unit="F" />
                <Tooltip cursor={{ strokeDasharray: '3 3' }} />
                <Legend verticalAlign="top"/>
                <Scatter name="Office" data={data[0]} fill="#FFBE33" />
                <Scatter name="Living Room" data={data[1]} fill="#82ca9d" />
                <Scatter name="Bedroom" data={data[2]} fill="#8884d8" />
                <Scatter name="Server Room" data={data[3]} fill="#0c0707" />
            </ScatterChart>
        )}
            {!(type === 'temp') && (
                <ScatterChart width={730} height={250}
                              margin={{ top: 20, right: 20, bottom: 10, left: 10 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="time" name="time" />
                    <YAxis dataKey="inside" name="humidity" unit="%" />
                    <ZAxis dataKey="outside" range={[64, 144]} name="outside humidity" unit="%" />
                    <Tooltip cursor={{ strokeDasharray: '3 3' }} />
                    <Legend verticalAlign="top"/>
                    <Scatter name="Office" data={data[0]} fill="#FFBE33" />
                    <Scatter name="Living Room" data={data[1]} fill="#82ca9d" />
                    <Scatter name="Bedroom" data={data[2]} fill="#8884d8" />
                    <Scatter name="Server Room" data={data[3]} fill="#0c0707" />
                </ScatterChart>
            )}
        </div>
    );
};

export default ScatterChart;

export const CustomizedAxisTick = ({x, y, stroke, payload}) => {
    return (
        <g transform={`translate(${x},${y})`}>
            <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-65)">{payload.value}</text>
        </g>
    );
}