import React, { useState, useMemo, useContext, useEffect } from 'react';

import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TableSortLabel, Toolbar, Typography,
         Box, Checkbox, Tooltip, IconButton } from '@mui/material';

import { alpha } from '@mui/material/styles';

import DeleteIcon from '@mui/icons-material/Delete';
import FilterListIcon from '@mui/icons-material/FilterList';
import CancelIcon from '@mui/icons-material/Cancel';

import "./globals.css"

// import { global } from '@/styles/global';
// import { Confirm } from '@/contexts/ConfirmContext';
// import { Message } from '@/contexts/MessageContext';
// import { deleter } from '@/api/deleter';

type Order = 'asc' | 'desc';

type ActionsProps = {
    id: string, 
    data: any[],
    reloadAction: () => void
}

type Columns = {
    id: string,
    lable: string

}

function stableSort<T>(array: readonly T[], comparator: (a: T, b: T) => number) {
    const stabilizedThis = array.map((el, index) => [el, index] as [T, number]);

    stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) {
            return order;
        }
        return a[1] - b[1];
    });

    return stabilizedThis.map((el) => el[0]);
}

function descendingComparator<T>(a : T, b : T, orderBy: keyof T) {
    if (b[orderBy] < a[orderBy]) {
        return -1;
    }
    if (b[orderBy] > a[orderBy]) {
        return 1;
    }
    return 0;
}

function getComparator<Key extends keyof any>(order: Order, orderBy: Key) :
    (
        a: { [key in Key] : number | string },
        b: { [key in Key] : number | string }
    ) => number {
    return order === 'desc'
        ? (a, b) => descendingComparator(a, b, orderBy)
        : (a, b) => -descendingComparator(a, b, orderBy);
}

const EnhancedTableToolbar = (props : {
    title : string,
    numSelected : number,
    useCheckbox : boolean,
    checkbox: boolean,
    handleCheckboxShowUp: () => void,
    handleCheckboxGoAway: () => void,
    handleDeleteSelect: (event : React.MouseEvent<HTMLButtonElement, MouseEvent>) => void,
}) => {
    const { title, numSelected, handleCheckboxShowUp, handleCheckboxGoAway, handleDeleteSelect, useCheckbox, checkbox } = props;

    return (
        <Toolbar
            sx={{
                pl: { sm: 2 },
                pr: { xs: 1, sm: 1 },
                ...(numSelected > 0 && {
                    bgcolor: (theme) =>
                    alpha(theme.palette.primary.main, theme.palette.action.activatedOpacity),
                }),
            }}
        >
            {numSelected > 0 ? (
                <Typography
                    sx={{ flex: '1 1 100%' }}
                    color="inherit"
                    variant="subtitle1"
                    component="div"
                >
                    {numSelected} Seleccionados
                </Typography>
                ) : (
                <Typography
                    sx={{ flex: '1 1 100%' }}
                    variant="h5"
                    id="tableTitle"
                    component="div"
                >
                    {title}
                </Typography>
            )}

            {useCheckbox &&
                <>
                    {numSelected > 0 &&
                        <Tooltip title="Delete">
                            <IconButton onClick={(event) => handleDeleteSelect(event)}>
                                <DeleteIcon />
                            </IconButton>
                        </Tooltip>
                    }
                    {checkbox ?
                        <Tooltip title="Cancelar">
                        <IconButton onClick={() => handleCheckboxGoAway()}>
                            <CancelIcon />
                        </IconButton>
                    </Tooltip> :
                        <Tooltip title="Multiselección">
                            <IconButton onClick={() => handleCheckboxShowUp()}>
                                <FilterListIcon />
                            </IconButton>
                        </Tooltip>
                    }
                </>
            }
        </Toolbar>
    );
}

function useTableComponent<T extends {id : string}> (props : { 
    rows : T[], 
    columns : Columns[], 
    url : string, 
    reloadTable : () => void
}) {
    const { rows, columns, url, reloadTable } = props;
    // const { confirm, setMessage } = useContext(Confirm);
    // const { handleOpen, setMessage: setStatusMessage, setStatus } = useContext(Message);
    const [page, setPage] = useState<number>(0);
    const [rowsPerPage, setRowsPerPage] = useState<number>(10)
    const [checkboxAction, setCheckboxAction] = useState<boolean>(false);
    const [order, setOrder] = useState<Order>('asc');
    const [orderBy, setOrderBy] = useState(columns[0].id);
    const [selected, setSelected] = useState<readonly string[]>([]);

    // useEffect(() => {
    //     setMessage('¿Seguro quieres eliminar todos estos datos?');
    // }, [])

    // const handleDeleteSelect = async (event) => {
    //     try {
    //         if(!event.shiftKey) {
    //             await confirm()
    //                 .catch(() => {throw {canceled: true}});
    //         }
    
    //         const response = await deleter({body: selected, url: url})
    
    //         if(response.status >= 200 && response.status <= 299) {
    //             setStatus(response.status);
    //             setStatusMessage('Datos eliminados con exito.');
    //             handleOpen();
                
    //             if(reloadTable)
    //                 await reloadTable();
    
    //             setSelected([]);
    //             setCheckboxAction(false);
    //         }
    //         else 
    //             throw {message: 'Ha habido un error al momento de eliminar.', status: response.status}
    //     }
    //     catch (err) {
    //         if(!err.canceled) {
    //             setStatus(err.status);
    //             setStatusMessage(err.message);
    //             handleOpen();
    //         }
    //     }
    // }

    const handleChangePage = (event : unknown, newPage : number) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event : React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleCheckboxShowUp = () => {
        setCheckboxAction(true);
    }

    const handleCheckboxGoAway = () => {
        setSelected([]);
        setCheckboxAction(false);
    }

    const handleSelectAll = (event : React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.checked) {
            const newSelected = rows.map((n) => n.id);
            setSelected(newSelected);
            return;
        }

        setSelected([]);
    }

    const handleRequestSort = (
        event : React.MouseEvent<unknown>, 
        property : any
    ) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    }

    const createSortHandler = (property: any) => (event: React.MouseEvent<unknown>) => {
        handleRequestSort(event, property);
    };

    const handleClick = (event : React.MouseEvent<unknown>, id : string) => {
        const selectedIndex = selected.indexOf(id);
        let newSelected : readonly string[] = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, id);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }

        setSelected(newSelected);
    }

    const isSelected = (id : string) => selected.indexOf(id) !== -1

    const visibleRows = useMemo(
        () =>
          stableSort(rows, getComparator(order, orderBy)).slice(
            page * rowsPerPage,
            page * rowsPerPage + rowsPerPage,
          ),
        [order, orderBy, page, rowsPerPage, rows],
    );

    return {
        page,
        rowsPerPage,
        checkboxAction,
        selected,
        order,
        orderBy,
        // handleDeleteSelect,
        handleCheckboxShowUp,
        handleCheckboxGoAway,
        handleSelectAll,
        handleChangeRowsPerPage,
        handleChangePage,
        handleClick,
        createSortHandler,
        visibleRows,
        isSelected
    };
}

export default function TableComponent<T extends { id : string }> (props : {
    title : string, 
    columns : Columns[], 
    rows : T[], 
    useCheckbox : boolean, 
    minWidth : number, 
    Actions : React.FC<ActionsProps>, 
    doubleClick : () => void, 
    reloadTable : () => void, 
    url : string
}) {
    const { title, columns, rows, useCheckbox, minWidth, Actions, doubleClick, reloadTable, url } = props;
    const { page, rowsPerPage, checkboxAction, orderBy, selected,
            handleCheckboxShowUp, handleSelectAll, handleChangeRowsPerPage,
            handleChangePage, handleCheckboxGoAway, handleClick, visibleRows,
            isSelected, order, createSortHandler
        } = useTableComponent({ rows, columns, url, reloadTable });

    return (
        <Paper className='table-general'>
            <Box sx={{margin: 2}}>
                <EnhancedTableToolbar
                    title={title}
                    handleCheckboxShowUp={handleCheckboxShowUp}
                    handleCheckboxGoAway={handleCheckboxGoAway}
                    handleDeleteSelect={handleDeleteSelect}
                    numSelected={selected.length}
                    useCheckbox={useCheckbox}
                    checkbox={checkboxAction}
                />
                <TableContainer sx={{ maxHeight: 1000 }}>
                    <Table stickyHeader aria-label="sticky table" >
                        <TableHead>
                            <TableRow
                                key='headerTable'
                            >
                                {checkboxAction &&
                                    <TableCell
                                        padding="checkbox">
                                        <Checkbox
                                            color="primary"
                                            indeterminate={selected.length > 0 && selected.length < rows.length}
                                            checked={rows.length > 0 && selected.length === rows.length}
                                            onChange={handleSelectAll}
                                            inputProps={{
                                                'aria-label': 'select all desserts',
                                            }}
                                        />
                                    </TableCell>
                                }
                                {columns.map((column) => (
                                    <>
                                        {column.id !== 'actions' ?
                                            <TableCell
                                                key={column.id}
                                                style={{ minWidth: minWidth }}
                                                sx={{textAlign: 'left' }}
                                                sortDirection={(orderBy === column.id ? order : false)}
                                            >
                                                <TableSortLabel
                                                    active={orderBy === column.id}
                                                    direction={orderBy === column.id ? order : 'asc'}
                                                    onClick={createSortHandler(column.id)}
                                                >
                                                    <Typography variant='subtitle1' sx={{fontWeight: '700'}}>
                                                        {column.label}
                                                    </Typography>
                                                </TableSortLabel>
                                            </TableCell> :
                                            !checkboxAction &&
                                                <TableCell
                                                    key={column.id}
                                                    style={{ minWidth: minWidth }}
                                                    sx={{textAlign: 'center' }}
                                                >
                                                    <Typography variant='subtitle1' sx={{fontWeight: '700'}}>
                                                        {column.label}
                                                    </Typography>
                                                </TableCell>
                                        }
                                    </> 
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {visibleRows.map((row, index) => {
                                const isItemSelected = isSelected(row.id);
                                const labelId = `enhanced-table-checkbox-${index}`;

                                return (
                                    <TableRow
                                        key={row.id}
                                        hover
                                        onClick={(event) => {
                                            if(checkboxAction)
                                                handleClick(event, row.id)}
                                        }
                                        onDoubleClick={() => {
                                            if(!checkboxAction)
                                                doubleClick(row.id)
                                        }}
                                        role="checkbox"
                                        tabIndex={-1}
                                        aria-checked={isItemSelected}
                                        selected={isItemSelected}
                                    >
                                        {checkboxAction &&
                                            <TableCell
                                                padding="checkbox"
                                            >
                                                <Checkbox
                                                    color="primary"
                                                    checked={isItemSelected}
                                                    inputProps={{
                                                        'aria-labelledby': labelId,
                                                    }}
                                                />
                                            </TableCell>
                                        }
                                        {columns.map((column) => {
                                            const key = column.id;

                                            if(key !== 'actions') {
                                                const value = row[column.id];

                                                if (Array.isArray(value)) {
                                                    return (
                                                        <TableCell key={key}>
                                                            {value.join(' - ')}
                                                        </TableCell>
                                                    );
                                                } else if (typeof value == 'object') {
                                                    return (
                                                        <TableCell key={key}>
                                                            <TableCellUsers
                                                                index={key}
                                                                row={value}
                                                            />
                                                        </TableCell>
                                                    );
                                                } else {
                                                    return (
                                                        <TableCell key={key}>
                                                            {value}
                                                        </TableCell>
                                                    );
                                                }
                                            }
                                            else {
                                                if(!checkboxAction)
                                                    return (
                                                        <TableCell
                                                            key={key}
                                                            sx={{textAlign: 'center'}}
                                                        >
                                                            <Actions
                                                                id={row.id}
                                                                data={rows}
                                                                reloadAction={reloadTable}
                                                            />
                                                        </TableCell>
                                                    );
                                            }
                                        })}
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[10, 25, 100]}
                    component="div"
                    count={rows.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    labelRowsPerPage='Filas por pagina'
                />
            </Box>
        </Paper>
    );
}