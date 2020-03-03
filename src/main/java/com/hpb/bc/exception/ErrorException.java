/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.exception;


import io.hpb.web3.protocol.core.Response.Error;

public class ErrorException extends RuntimeException {

	private static final long serialVersionUID = 8260240686788875620L;
	    
    private Error error;

	public ErrorException(int code, String message) {
		error = new Error(code, message);
    }
	
	public ErrorException(Error error) {
        this.error = error; 
    }

    public int getCode() {
        return error.getCode();
    }

    public void setCode(int code) {
        this.error.setCode(code);
    }

    public String getMessage() {
        return error.getMessage();
    }

    public void setMessage(String message) {
    	 this.error.setMessage(message);
    }

    public String getData() {
        return  this.error.getData();
    }

    public void setData(String data) {
    	 this.error.setData(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Error)) {
            return false;
        }

        Error error = (Error) o;

        if (getCode() != error.getCode()) {
            return false;
        }
        if (getMessage() != null
                ? !getMessage().equals(error.getMessage()) : error.getMessage() != null) {
            return false;
        }
        return getData() != null ? getData().equals(error.getData()) : error.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getCode();
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }
}
