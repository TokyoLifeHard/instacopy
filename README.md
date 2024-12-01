# Instacopy
___
## Description
This is pet-project instagram`s backend.  
Project can:
1. Registration users
2. Create post via user.
3. Comment post via user
4. Add picture to post
___
## Architect
![image](src/main/resources/static/instacopy_arc.png)
---
## Data model
![image](src/main/resources/static/data_model_instacopy.PNG)
___
## Resources
| Usage         | Resource                                                                                                                           |
|---------------|------------------------------------------------------------------------------------------------------------------------------------|
| Authorization | 1.POST api/auth/singin<br/>2.POST api/auth/singup                                                                                  |
| User          | 1.GET api/user/ <br/>2.GET api/user/:userId <br/>3.POST api/auth/update<br/>4.GET api/user/search/:username                        |
| Post          | 1.POST api/post/create <br/>2.POST api/post/:postId/delete <br/>3.GET api/post/all<br/>4.GET api/post/user/posts                   |
| Comment       | 1.POST api/comment/:postId/create <br/>2.GET api/comment/:postId/all <br/>3.POST api/comment/:commentId/delete<br/>                |
| Image         | 1.POST api/image/upload <br/>2.POST api/image/:postId/upload <br/>3.GET api/image/profileImage<br/> 4.POST api/image/:postId/image |

