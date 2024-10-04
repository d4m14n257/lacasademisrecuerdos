"use client"

import { BaseSyntheticEvent, useCallback, useContext, useRef } from "react";
import { z } from "zod";
import { Advice } from "@/contexts/AdviceProvider";
import { Confirm } from "@/contexts/ConfirmContext";
import { ErrorMessage } from "@hookform/error-message";
import { zodResolver } from "@hookform/resolvers/zod";
import { Divider, FormControl, FormHelperText, Stack } from "@mui/material";
import { useSession } from "next-auth/react";
import { FileUploader } from "react-drag-drop-files";
import { Controller, ControllerRenderProps, SubmitHandler, useForm } from "react-hook-form";
import { LoadingButton } from "@mui/lab";
import SendIcon from '@mui/icons-material/Send';

import "./form.css"
import { setData } from "@/api/setData";

type Props = {
    roomId: string;
    handleClose: () => void;
    reloadAction: () => Promise<void>;
}

const schema = z.object({
    file: z.boolean({
        required_error: "File is required"
    })
})

type FileSchema = z.infer<typeof schema>

function useFileForm () {
    const { data: session } = useSession();
    const { confirm, handleMessage } = useContext(Confirm);
    const { handleOpen, handleAdvice } = useContext(Advice);
    const file = useRef<File[] | null>(null);
    const fileTypes = ['png', 'jpg', 'jpeg'];

    const handleLoadFile : (file : File[], field : ControllerRenderProps<FileSchema>) => void = (fileAdd, field) => {
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
    }
}

export default function FileForm (props : Props) {
    const { roomId, handleClose, reloadAction } = props;
    const { file, token, confirm, fileTypes, handleLoadFile, handleMessage, handleOpen, handleAdvice } = useFileForm();

    const { control, handleSubmit, formState: { errors, isSubmitting } } = useForm<FileSchema>({
        defaultValues: {
            file: undefined
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<FileSchema> = useCallback(async (data, event?: BaseSyntheticEvent) => {
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
                title: "Are you sure you want to change the main image?",
                content: 'Once the main image is changed, the previous one will be saved in the list of images'
            })

            await confirm()
                .catch(() => {throw {canceled: true}})

            if(data.file && file.current) {
                const formData = new FormData();
                const jsonData = JSON.stringify({
                    id: roomId
                })

                const blob = new Blob([jsonData], { type: 'application/json' });
                formData.append('data', blob);

                for (let i = 0; i < file.current.length; i++) {
                    formData.append('files', file.current[i]);
                }

                const res = await setData<FormData>('file/admin/add/room', token, formData, "PUT");

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
                        console.log('Whats happened?!');
                    }
                    else {
                        handleAdvice({
                            message: res.message || res.err,
                            status: res.status
                        })

                        handleOpen();
                    }
                }

                return;
            }
            else {
                handleAdvice({
                    message: 'File not found',
                    status: 400
                })

                handleOpen();
            }

        }
        catch (err : unknown) {
            if(err instanceof Error) {
                handleAdvice({
                    message: err.message.charAt(0).toUpperCase() + err.message.slice(1),
                    status: 503
                })
            }
            if(typeof err === 'object' && err !== null && 'canceled' in err) {
                return;
            }
            else {
                handleAdvice({
                    message: 'Unknown error',
                    status: 503
                })
            }

            handleOpen();
        }
    }, [])

    return (
        <form className="content">
            <Divider />
            <Controller
                name='file'
                control={ control }
                render={({ field }) => 
                    <FormControl
                        error={Boolean(errors.file)}
                        className='label-field'
                    >
                        <FileUploader 
                            multiple
                            types={fileTypes}
                            handleChange={(file : File[]) => 
                                handleLoadFile(file, field)}
                        />
                        {errors.file ?
                            <FormHelperText>
                                <ErrorMessage errors={errors} name="file"/>
                            </FormHelperText> :
                            <FormHelperText>
                                You must upload images/an image of the room that will be shown
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
            </Stack>
        </form>
    );
}