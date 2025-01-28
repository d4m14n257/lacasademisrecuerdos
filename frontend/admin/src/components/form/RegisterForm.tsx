"use client"

import { BaseSyntheticEvent, useCallback, useContext } from 'react';
import { FormControl, FormControlLabel, FormHelperText, Stack, TextField, Typography } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import { z } from 'zod';
import { Advice } from '@/contexts/AdviceProvider';
import { SubmitHandler, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { ErrorMessage } from '@hookform/error-message';
import { setData } from '@/api/setData';
import { REGISTER } from '@/constants/endpoints';
import { Confirm } from '@/contexts/ConfirmContext';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import './form.css'

const schema = z.object({
    email: z.string().nonempty("Email is required."),
    username: z.string().nonempty("Username is required."),
    first_name: z.string().nonempty("First name is required."),
    last_name: z.string().nonempty("Last name is required."),
    password: z.string().nonempty("Password is required."),
    confirmPassword: z.string().nonempty("Confirm Password is required."),
}).refine(
    (data) => data.password === data.confirmPassword, {
    message: "Password don't match.",
    path: ["confirmPassword"]
})

type Register = z.infer<typeof schema>

const useRegisterForm = () => {
    const { handleOpen, handleAdvice } = useContext(Advice); 
    const { confirm, handleMessage } = useContext(Confirm);
    
    return {
        confirm,
        handleOpen,
        handleAdvice,
        handleMessage
    }
}

export default function RegisterForm() {
    const { confirm, handleOpen, handleAdvice, handleMessage } = useRegisterForm();
    const { register, handleSubmit, setError, reset, formState: { errors, isSubmitting }} = useForm<Register>({
        defaultValues: {
            email: '',
            username: '',
            first_name: '',
            last_name: '',
            password: '',
            confirmPassword: ''
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<Register> = useCallback(async (data, event?: BaseSyntheticEvent) => {
        event?.preventDefault();

        try {
            handleMessage({
                title: "Are you sure your data is correct?",
                content: "Once the user has been created, you must wait for it to be authorized by the administrator so that you can access it."
            })

            await confirm()
                .catch(() => {throw {canceled: true}})
            
            const res = await setData<Omit<Register, "confirmPassword">>(REGISTER, data, "POST");
            
            if(res.status >= 200 && res.status <= 299) {
                handleAdvice ({
                    message: res.message,
                    status: res.status
                })

                reset({
                    email: '',
                    username: '',
                    first_name: '',
                    last_name: '',
                    password: '',
                    confirmPassword: ''
                })
                handleOpen();
            }
            else {
                if(res.errors) {
                    Object.entries(res.errors).forEach(([key, value]) => {
                        setError(key as keyof Register, { message: value });
                    })
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
        catch (err: unknown) {
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
            <FormControl
                fullWidth
                className='label-field'
                error={Boolean(errors)}
            >
                <FormControlLabel
                    label={<Typography color={Boolean(errors.email) ? "error" : "info"}>Email</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.email)}
                            placeholder="your@email.com"
                            {...register("email")}
                            
                        />}
                />
                {errors.email && 
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="email" />
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                className='label-field'
                error={Boolean(errors)}
            >
                <FormControlLabel
                    label={<Typography color={Boolean(errors.username) ? "error" : "info"}>Username</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.username)}
                            placeholder="YourUsername"
                            {...register("username")}
                            
                        />}
                />
                {errors.username && 
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="username" />
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                className='label-field'
                error={Boolean(errors)}
            >
                <FormControlLabel
                    label={<Typography color={Boolean(errors.first_name) ? "error" : "info"}>First name</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.first_name)}
                            placeholder="First Name"
                            {...register("first_name")}
                            
                        />}
                />
                {errors.first_name && 
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="first_name" />
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                className='label-field'
                error={Boolean(errors)}
            >
                <FormControlLabel
                    label={<Typography color={Boolean(errors.last_name) ? "error" : "info"}>Last name</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.last_name)}
                            placeholder="Last Name"
                            {...register("last_name")}
                            
                        />}
                />
                {errors.last_name && 
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="last_name" />
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                className='label-field'
                error={Boolean(errors)}
            >
                <FormControlLabel
                    label={<Typography color={Boolean(errors.password) ? "error" : "info"}>Password</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.password)}
                            type="password"
                            placeholder="••••••"
                            {...register("password")}
                            
                        />}
                />
                {errors.password && 
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="password" />
                    </FormHelperText>
                }
            </FormControl>
            <FormControl
                fullWidth
                className='label-field'
                error={Boolean(errors)}
            >
                <FormControlLabel
                    label={<Typography color={Boolean(errors.confirmPassword) ? "error" : "info"}>Confirm password</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.confirmPassword)}
                            type="password"
                            placeholder="••••••"
                            {...register("confirmPassword")}
                            
                        />}
                />
                {errors.confirmPassword && 
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="confirmPassword" />
                    </FormHelperText>
                }
            </FormControl>
            <Stack direction="row" justifyContent="end">
                <LoadingButton
                    loading={isSubmitting}
                    variant='contained'
                    type='submit'
                    onClick={(event) => handleSubmit(onSubmit)(event)}
                    endIcon={<PersonAddIcon />}
                    sx={{ width: "100%" }}
                >
                    Register
                </LoadingButton>
            </Stack>
        </form>
    );
}