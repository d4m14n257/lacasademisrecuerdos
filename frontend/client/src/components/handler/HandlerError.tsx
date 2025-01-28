import React, { Children } from "react"

import './handler.css'

type Props = {
    children: React.JSX.Element[]
}

type PropsMessage = {
    status: number;
    message?: string;
}

export function HandlerMessageError (props : PropsMessage) {
    const { message, status } = props;

    return (
        <div className="handler-message">
            <h5> {status} </h5>
            <hr />
            <p> {message} </p>
        </div>
    );
}

export function HandlerError(props : Props) {
    let when : React.ReactElement | null = null;
    let error : React.ReactElement | null= null;

    Children.forEach(props.children, children => {

        if(children.props.hasError === undefined) {
            error = children
        }
        else if(!when && children.props.hasError === false) {
            when = children;
        }
    })

    return when || error;
}

HandlerError.When = ({ hasError, children } : { hasError : boolean, children : React.ReactElement }) => !hasError && children;
HandlerError.Else = ({ render, children } : { render?: JSX.Element, children?: React.ReactElement }) => render || children;