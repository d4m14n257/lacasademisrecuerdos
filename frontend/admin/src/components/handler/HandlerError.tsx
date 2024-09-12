import React, { Children } from "react"

type Props = {
    children: React.JSX.Element[]
}

export function HandlerError(props : Props) {
    let when : React.ReactElement | null = null;
    let error : React.ReactElement | null= null;

    Children.forEach(props.children, children => {
        if(children.props.isError === undefined) {
            error = children
        }
        else if(!when && children.props.isError === false) {
            when = children;
        }
    })

    return when || error;
}

HandlerError.When = ({ isError, children } : { isError : boolean, children : React.ReactElement }) => !isError && children;
HandlerError.Else = ({ render, children } : { render?: JSX.Element, children?: React.ReactElement }) => render || children;