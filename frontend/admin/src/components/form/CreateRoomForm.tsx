"use client"

import SendIcon from '@mui/icons-material/Send';
import CloseIcon from '@mui/icons-material/Close';
import { FileUpload } from "@mui/icons-material";
import { BaseSyntheticEvent, useCallback, useState } from "react";
import { Controller, ControllerRenderProps, SubmitHandler, useForm } from "react-hook-form";
import { z } from "zod";

import "./form.css"
import { FormControl, FormControlLabel, FormHelperText, Stack, TextField, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { ErrorMessage } from '@hookform/error-message';
import { FileUploader } from 'react-drag-drop-files';
import TextArea from './TextArea';

type Props = {
    handleClose: () => void
}

const schema = z.object({
    name: z.string({
        required_error: "Name is required",
    }).max(32, "The name cannot be longer than 32 characters"),
    description: z.string({
        required_error: "Description is required"
    }).max(2048, "The name cannot be longer than 2048 characters"),
    summary: z.string({
        required_error: "Summay is required"
    }).max(512, "The name cannot be longer than 512 characters"),
    additional: z.string({
        required_error: "Additional is required"
    }).max(128, "The name cannot be longer than 128 characters"),
    single_price: z.string({
        required_error: "Single price is required"
    }).regex(/^\d{1,8}(\.\d{1,2})?$/, "It must be a number with up to 10 digits and up to 2 decimals"),
    double_price: z.string().regex(/^\d{1,8}(\.\d{1,2})?$/, "It must be a number with up to 10 digits and up to 2 decimals"),
    file: z.boolean({
        required_error: "File is required"
    })
})

type Room = z.infer<typeof schema>

const useCreateRoomForm = () => {
    const [ file, setFile ] = useState<File | null>(null);
    const fileTypes = ['png', 'jpg', 'jpeg'];

    const handleLoadFile : (file : File, field : ControllerRenderProps<Room>) => void = (file, field) => {
        console.log(File)
    }

    return {
        fileTypes, 
        handleLoadFile
    };
}

export default function CreateRoomForm (props : Props) {
    const { handleClose } = props;
    const { fileTypes, handleLoadFile } = useCreateRoomForm();

    const { control, register, handleSubmit, formState: { errors, isSubmitting }} = useForm<Room>({
        defaultValues: {
            name: "",
            description: "",
            summary: "",
            additional: "",
            single_price: "",
            double_price: "",
            file: false
        }
    })

    const onSubmit : SubmitHandler<Room> = useCallback(async (data, event?: BaseSyntheticEvent) => {
        event?.preventDefault();

        try {
            console.log(data)
        }
        catch (err : unknown) {

        }
    }, [])
    
    return (
        <form className="content">
            <FormControl
                fullWidth
                error={Boolean(errors)}
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
                    <FormHelperText>
                        The name that the room will have
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                error={Boolean(errors)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.description) ? "error" : "info"}>Description</Typography>}
                    labelPlacement='top'
                    control={
                        <TextArea 
                            disabled={isSubmitting}
                            error={Boolean(errors.description)}
                            placeholder='Azul'
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
                error={Boolean(errors)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.additional) ? "error" : "info"}>Additional</Typography>}
                    labelPlacement='top'
                    control={
                        <TextArea 
                            disabled={isSubmitting}
                            error={Boolean(errors.additional)}
                            placeholder='Azul'
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
                error={Boolean(errors)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.summary) ? "error" : "info"}>Summary</Typography>}
                    labelPlacement='top'
                    control={
                        <TextArea 
                            disabled={isSubmitting}
                            error={Boolean(errors.summary)}
                            placeholder='Azul'
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
                error={Boolean(errors)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.single_price) ? "error" : "info"}>Single price</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField 
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.single_price)}
                            placeholder='9999.99'
                            {...register('single_price')}
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
                error={Boolean(errors)}
                className='label-field'
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.double_price) ? "error" : "info"}>Single price</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField 
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.double_price)}
                            placeholder='9999.99'
                            {...register('double_price')}
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
                control={ control }
                render={({ field }) => 
                    <FormControl>
                        <FileUploader 
                            multiple={false}
                            type={fileTypes}
                            handleChage={(file : File) => handleLoadFile(file, field)}
                        />
                    </FormControl>
                }
                {...register("file")}
            />
            <FileUpload 

            />
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