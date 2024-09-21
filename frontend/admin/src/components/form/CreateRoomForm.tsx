"use client"

import { z } from "zod";
import SendIcon from '@mui/icons-material/Send';
import CloseIcon from '@mui/icons-material/Close';
import { BaseSyntheticEvent, useCallback, useContext, useRef, useState } from "react";
import { Controller, ControllerRenderProps, SubmitHandler, useForm } from "react-hook-form";
import { Divider, FormControl, FormControlLabel, FormHelperText, InputAdornment, InputLabel, OutlinedInput, Stack, TextField, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { ErrorMessage } from '@hookform/error-message';
import { FileUploader } from 'react-drag-drop-files';
import TextArea from './TextArea';
import { Confirm } from '@/contexts/ConfirmContext';
import { zodResolver } from '@hookform/resolvers/zod';
import { setData } from '@/api/setData';
import { useSession } from 'next-auth/react';
import { Advice } from '@/contexts/AdviceProvider';

import "./form.css"

type Props = {
    handleClose: () => void,
    reloadAction: () => Promise<void>
}

const schema = z.object({
    name: z.string()
    .max(32, { message: "The name cannot be longer than 32 characters"})
    .min(1, { message: "Name is required"}),
    description: z.string()
    .max(2048, {message: "The name cannot be longer than 2048 characters"})
    .min(1, { message: "Description is required" }),
    summary: z.string()
    .max(512, { message: "The name cannot be longer than 512 characters" })
    .min(1, { message: "Summary is required" }),
    additional: z.string()
    .max(128, { message: "The name cannot be longer than 128 characters"})
    .min(1, { message: "Additional is required"}),
    single_price: z.number({
        required_error: 'Single prices is required'
    })
    .refine((val) => { 
            return val >= 0 && val < 100000000 && Number(val.toFixed(2)) === val;
        }, 
        { message: "It must be a number with up to 8 digits and up to 2 decimals" }
    ),
    double_price: z.number()
    .refine((val) => { 
            return val >= 0 && val < 100000000 && Number(val.toFixed(2)) === val;
        }, 
        { message: "It must be a number with up to 8 digits and up to 2 decimals" }
    ),
    file: z.boolean({
        required_error: "File is required"
    })
})

type Room = z.infer<typeof schema>

const useCreateRoomForm = () => {
    const { data: session } = useSession();
    const { confirm, handleMessage } = useContext(Confirm);
    const { handleOpen, handleAdvice } = useContext(Advice);
    const file = useRef<File | null>();
    const fileTypes = ['png', 'jpg', 'jpeg'];

    const handleLoadFile : (file : File, field : ControllerRenderProps<Room>) => void = (fileAdd, field) => {
        if(fileAdd) {
            file.current = fileAdd;
            field.onChange(true);
        }
        else {
            file.current = null;
            field.onChange(false);
        }
    }

    return {
        file,
        token: session?.token,
        confirm,
        fileTypes, 
        handleLoadFile,
        handleMessage,
        handleOpen,
        handleAdvice
    };
}

export default function CreateRoomForm (props : Props) {
    const { handleClose, reloadAction } = props;
    const { file, token, confirm, fileTypes, handleLoadFile, handleMessage, handleOpen, handleAdvice } = useCreateRoomForm();

    const { control, register, handleSubmit, formState: { errors, isSubmitting }} = useForm<Room>({
        defaultValues: {
            name: undefined,
            description: undefined,
            summary: undefined,
            additional: undefined,
            single_price: undefined,
            double_price: undefined,
            file: undefined
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<Room> = useCallback(async (data, event?: BaseSyntheticEvent) => {
        event?.preventDefault();
        try {
            if(!token) {
                handleAdvice({
                    message: 'Invalid session, restart the session',
                    status: 401
                })

                handleOpen();
                return;
            }

            handleMessage({
                title: "Are you sure you want to save this room?",
                content: 'Once the room is created, it has to be activated so that it can be displayed on the main page'
            })

            await confirm()
                .catch(() => {throw {canceled: true}})

            if(data.file && file.current) {
                const formData = new FormData();
                const jsonData = JSON.stringify({
                    ...data,
                    file: undefined
                })
                formData.append('file', file.current);

                const blob = new Blob([jsonData], { type: 'application/json' });
                formData.append('data', blob);

                const res = await setData<FormData>('room/admin', token, formData);

                if(res.status >= 200 && res.status <= 299) {
                    await reloadAction();

                    handleAdvice({
                        message: res.message,
                        status: res.status
                    })

                    handleOpen();
                    handleClose();
                }
                else {
                    if(res.errors) {
                        console.log('no se que hacer aqui xd');
                    }
                    else {
                        handleAdvice({
                            message: res.message,
                            status: res.status
                        })

                        handleOpen();
                    }
                }
            }
            else {
                handleAdvice({
                    message: 'File not found',
                    status: 400
                })
            }
        }
        catch (err : unknown) {
            if(err instanceof Error) {
                handleAdvice({
                    message: err.message.charAt(0).toUpperCase() + err.message.slice(1),
                    status: 503
                })
            }

            return;
        }
    }, [])

    return (
        <form className="content">
            <Divider />
            <FormControl
                fullWidth
                error={Boolean(errors.name)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.name) ? "error" : "info"}>Room name</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField 
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.name)}
                            placeholder='Azul'
                            {...register('name')}
                        />
                    }
                />
                {errors.name ?
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="name"/>
                    </FormHelperText> :
                    <FormHelperText
                        error={Boolean(errors.name)}
                    >
                        The name that the room will have
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                error={Boolean(errors.description)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.description) ? "error" : "info"}>Description</Typography>}
                    labelPlacement='top'
                    control={
                        <TextArea 
                            disabled={isSubmitting}
                            error={Boolean(errors.description)}
                            placeholder='This beautiful room is located on the second floor of the house...'
                            minRows={4}
                            {...register('description')}
                        />
                    }
                />
                {errors.description ?
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="description"/>
                    </FormHelperText> :
                    <FormHelperText>
                        You must put a detailed description of the room
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                error={Boolean(errors.additional)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.additional) ? "error" : "info"}>Additional</Typography>}
                    labelPlacement='top'
                    control={
                        <TextArea 
                            disabled={isSubmitting}
                            error={Boolean(errors.additional)}
                            placeholder='1 king-sized bed or 2 single beds. Private Bath.'
                            minRows={4}
                            {...register('additional')}
                        />
                    }
                />
                {errors.additional ?
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="additional"/>
                    </FormHelperText> :
                    <FormHelperText>
                        You must provide information describing what the room includes
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                error={Boolean(errors.summary)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.summary) ? "error" : "info"}>Summary</Typography>}
                    labelPlacement='top'
                    control={
                        <TextArea 
                            disabled={isSubmitting}
                            error={Boolean(errors.summary)}
                            placeholder='Located on the second floor of the house, this room has...'
                            minRows={4}
                            {...register('summary')}
                        />
                    }
                />
                {errors.summary ?
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="summary"/>
                    </FormHelperText> :
                    <FormHelperText>
                        You must put a summary of the room to show to the users
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                error={Boolean(errors.single_price)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.single_price) ? "error" : "info"}>Single price</Typography>}
                    labelPlacement='top'
                    control={
                        <OutlinedInput
                            id="single-price"
                            fullWidth
                            disabled={isSubmitting}
                            type='number'
                            placeholder='9999.99'
                            startAdornment={<InputAdornment position="start">$</InputAdornment>}
                            {...register('single_price', {
                                setValueAs: (value) => value === "" ? undefined : parseFloat(value)
                            })}
                        />
                    }
                />
                {errors.single_price ?
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="single_price"/>
                    </FormHelperText> :
                    <FormHelperText>
                        You must have the price of the room
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                error={Boolean(errors.double_price)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.double_price) ? "error" : "info"}>Double price</Typography>}
                    labelPlacement='top'
                    control={
                        <OutlinedInput
                            id="single-price"
                            fullWidth
                            disabled={isSubmitting}
                            type='number'
                            placeholder='9999.99'
                            startAdornment={<InputAdornment position="start">$</InputAdornment>}
                            {...register('double_price', {
                                setValueAs: (value) => value === "" ? undefined : parseFloat(value) 
                            })}
                        />
                    }
                />
                {errors.double_price ?
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="double_price"/>
                    </FormHelperText> :
                    <FormHelperText>
                        If you have one, you can put the price of a double room
                    </FormHelperText>
                }
            </FormControl>
            <Controller
                name='file'
                control={ control }
                render={({ field }) => 
                    <FormControl
                        error={Boolean(errors.file)}
                        className='label-field'
                    >
                        <FileUploader 
                            multiple={false}
                            types={fileTypes}
                            handleChange={(file : File) => 
                                handleLoadFile(file, field)}
                        />
                        {errors.file ?
                            <FormHelperText>
                                <ErrorMessage errors={errors} name="file"/>
                            </FormHelperText> :
                            <FormHelperText>
                                You must upload an image of the room that will be shown
                            </FormHelperText>
                        }
                    </FormControl>
                }
            />
            <Divider />
            <Stack direction="row-reverse" justifyContent='flex-start' spacing={2}>
                <LoadingButton
                    loading={isSubmitting}
                    variant='contained'
                    type='submit'
                    onClick={(event) => handleSubmit(onSubmit)(event)}
                    endIcon={<SendIcon />}
                    color='success'
                >
                    Submit
                </LoadingButton>
                <LoadingButton 
                    loading={isSubmitting}
                    variant='contained'
                    onClick={handleClose}
                    endIcon={<CloseIcon />}
                >
                    Close
                </LoadingButton>
            </Stack>
        </form>
    );
}