(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('classe', {
            parent: 'entity',
            url: '/classe?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Classes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classe/classes.html',
                    controller: 'ClasseController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('classe-detail', {
            parent: 'entity',
            url: '/classe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Classe'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classe/classe-detail.html',
                    controller: 'ClasseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Classe', function($stateParams, Classe) {
                    return Classe.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'classe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('classe-detail.edit', {
            parent: 'classe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-dialog.html',
                    controller: 'ClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classe', function(Classe) {
                            return Classe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classe.new', {
            parent: 'classe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-dialog.html',
                    controller: 'ClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                cycle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('classe', null, { reload: 'classe' });
                }, function() {
                    $state.go('classe');
                });
            }]
        })
        .state('classe.edit', {
            parent: 'classe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-dialog.html',
                    controller: 'ClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classe', function(Classe) {
                            return Classe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classe', null, { reload: 'classe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classe.delete', {
            parent: 'classe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-delete-dialog.html',
                    controller: 'ClasseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Classe', function(Classe) {
                            return Classe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classe', null, { reload: 'classe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
