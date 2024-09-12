"use client"

import { BaseSyntheticEvent, useCallback, useContext } from "react";

import { FormControl, FormControlLabel, FormHelperText, FormLabel, Stack, TextField, Typography } from "@mui/material";

import { SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ErrorMessage } from "@hookform/error-message";
import { z } from "zod";

import LoadingButton from '@mui/lab/LoadingButton';
import LoginIcon from '@mui/icons-material/Login';

import './form.css'
import { signIn } from "next-auth/react";
import { Advice } from "@/contexts/AdviceProvider";
import { useRouter } from "next/navigation";

const schema = z.object({
    username: z.string().nonempty("Email or Username is required"),
    password: z.string().min(1, "Password is required")
})

type Login = z.infer<typeof schema>

const useLoginForm = () => {
    const router = useRouter();
    const { handleOpen, handleAdvice } = useContext(Advice);

    return {
        router,
        handleOpen,
        handleAdvice
    }
}

export default function LoginForm () {
    const {router, handleOpen, handleAdvice} = useLoginForm();
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm<Login>({
        defaultValues: {
            username: '',
            password: ''
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<Login> = useCallback(async (data, event?: BaseSyntheticEvent) => {
        event?.preventDefault();

        try {
            const response = await signIn("credentials", {
                ...data,
                redirect: false
            });
            
            if(response?.error) {
                console.log(response);

                const value = {
                    message: response.error,
                    status: response.status
                }

                handleAdvice(value);
                handleOpen();
            }
            else {
                router.push('/')
            }
            
        }
        catch (err) {
            console.log(err)
        }
    }, []);

    return(
        <form className="content">
            <FormControl 
                fullWidth
                error={Boolean(errors)}
                className="label-field"
            >   
                <FormControlLabel
                    label={<Typography color={Boolean(errors.username) ? "error" : "info"}>Email or username</Typography>}
                    labelPlacement='top'
                    control={
                        <TextField 
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.username)}
                            placeholder="your@email.com"
                            {...register("username")}
                        />}
                />
                { errors.username &&
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="username"/>
                    </FormHelperText>
                }
            </FormControl>
            <FormControl 
                fullWidth
                error={Boolean(errors)}
                className="label-field"
            >
                <FormControlLabel 
                    label={<Typography color={Boolean(errors.password) ? "error" : "info"}>Password</Typography>}
                    labelPlacement="top"
                    control={
                        <TextField 
                            fullWidth
                            disabled={isSubmitting}
                            error={Boolean(errors.password)}
                            type="password"
                            placeholder="••••••"
                            {...register("password")}
                        />
                    }
                />
                { errors.password &&
                    <FormHelperText>
                        <ErrorMessage errors={errors} name="password"/>
                    </FormHelperText>
                }
            </FormControl>
            <Stack direction="row" justifyContent="end">
                <LoadingButton
                    loading={isSubmitting}
                    variant='contained' 
                    type='submit' 
                    onClick={(event) => handleSubmit(onSubmit)(event)}
                    endIcon={<LoginIcon />}
                    sx={{ width: "100%" }}
                >
                    Login
                </LoadingButton>
            </Stack>
        </form>
    );
}